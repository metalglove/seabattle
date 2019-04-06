package seabattlegame.listeners;

import common.MessageLogger;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.RegisterResponse;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegisterResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final String name;
    private final ISeaBattleGame game;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public RegisterResponseChangeListener(ISeaBattleGUI application, String name, ISeaBattleGame game, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.name = name;
        this.game = game;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RegisterResponse response = (RegisterResponse) evt.getNewValue();
        if (!response.isSuccess()) {
            application.showErrorMessage("Failed to register!");
            messageLogger.error("Failed to register!");
        } else {
            application.setPlayerNumber(response.getPlayerNumber(), name);
            game.setPlayerName(name);
            if (response.getOpponentName() != null && response.getOpponentPlayerNumber() != null) {
                application.setOpponentName(response.getOpponentPlayerNumber(), response.getOpponentName());
            } else {
                client.addListener(OpponentRegisterResponse.class.getSimpleName(), new OpponentRegisterResponseListener(application, client, messageLogger));
            }
        }
        client.removeListener(RegisterResponse.class.getSimpleName(), this);
    }
}
