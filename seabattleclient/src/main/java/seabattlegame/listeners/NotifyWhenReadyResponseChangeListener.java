package seabattlegame.listeners;

import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.messages.responses.OpponentFireShotResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NotifyWhenReadyResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final Client client;
    private final MessageLogger messageLogger;

    public NotifyWhenReadyResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, Client client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        NotifyWhenReadyResponse response = (NotifyWhenReadyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Notify from other player failed!");
            messageLogger.error("Notify from other player failed!");
        } else {
            game.setStarted(true);
            game.setPlayerTurn(response.isPlayersTurn);
            application.notifyStartGame(response.playerNumber);
            application.showErrorMessage(response.isPlayersTurn ? "You start." : "Opponent starts.");
            messageLogger.info(response.isPlayersTurn ? "Player starts." : "Opponent starts.");
            client.addListener(OpponentFireShotResponse.class.getSimpleName(), new OpponentFireShotResponseListener(application, game, client, messageLogger));
            client.removeListener(NotifyWhenReadyResponse.class.getSimpleName(), this);
        }
    }
}