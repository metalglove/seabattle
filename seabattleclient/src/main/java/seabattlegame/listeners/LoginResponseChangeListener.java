package seabattlegame.listeners;

import common.MessageLogger;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.LoginResponse;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.RegisterResponse;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final String name;
    private final ISeaBattleGame game;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public LoginResponseChangeListener(ISeaBattleGUI application, String name, ISeaBattleGame game, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.name = name;
        this.game = game;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoginResponse response = (LoginResponse) evt.getNewValue();
        if (!response.isSuccess()) { //TODO: If credentials incorrect, abort. Else if name doesnt exist, register.
            if (!response.getCredentialsCorrect()) {
                application.showErrorMessage("Invalid credentials!");
                messageLogger.error("Invalid credentials!");
                //  client.addListener(RegisterResponse.class.getSimpleName(), new RegisterResponseChangeListener(application, name, this, client, handlerMessageLogger));
                ////        client.addListener(ErrorResponse.class.getSimpleName(), new ErrorResponseChangeListener(application, this, client, handlerMessageLogger));
                ////        client.startWriting(new RegisterRequest(name, password, multiPlayer));
            } else {
                application.showErrorMessage("Failed to login!");
                messageLogger.error("Failed to login!");
            }
        } else {
            application.setPlayerNumber(response.getPlayerNumber(), name);
            game.setPlayerName(name);
            if (response.getOpponentName() != null && response.getOpponentPlayerNumber() != null) {
                application.setOpponentName(response.getOpponentPlayerNumber(), response.getOpponentName());
            } else {
                client.addListener(OpponentRegisterResponse.class.getSimpleName(), new OpponentRegisterResponseListener(application, client, messageLogger));
            }
        }
        client.removeListener(LoginResponse.class.getSimpleName(), this);
    }
}
