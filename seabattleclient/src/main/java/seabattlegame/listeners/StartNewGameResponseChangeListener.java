package seabattlegame.listeners;

import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.StartNewGameResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StartNewGameResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final String name;
    private final ObservableClientSocket client;
    private final ISeaBattleGame game;
    private final MessageLogger messageLogger;

    public StartNewGameResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, String name, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.name = name;
        this.client = client;
        this.game = game;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StartNewGameResponse response = (StartNewGameResponse) evt.getNewValue();
        if (!response.isSuccess()) {
            application.showErrorMessage("Starting a new game failed!");
            messageLogger.error("Starting a new game failed!");
        } else {
            game.resetGame();
            application.setPlayerNumber(response.getPlayerNumber(), name);
            if (response.getOpponentName() != null && response.getOpponentPlayerNumber() != null) {
                application.setOpponentName(response.getOpponentPlayerNumber(), response.getOpponentName());
            } else {
                client.addListener(OpponentRegisterResponse.class.getSimpleName(), new OpponentRegisterResponseListener(application, client, messageLogger));
            }
        }
        client.removeListener(StartNewGameResponse.class.getSimpleName(), this);
    }
}