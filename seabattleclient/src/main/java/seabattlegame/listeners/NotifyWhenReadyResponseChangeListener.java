package seabattlegame.listeners;

import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.messages.responses.OpponentFireShotResponse;
import seabattlegame.Client;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NotifyWhenReadyResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final MultiPlayerSeaBattleGame multiPlayerSeaBattleGame;
    private final int playerNumber;
    private final Client client;

    public NotifyWhenReadyResponseChangeListener(ISeaBattleGUI application, MultiPlayerSeaBattleGame multiPlayerSeaBattleGame, int playerNumber, Client client) {
        this.application = application;
        this.multiPlayerSeaBattleGame = multiPlayerSeaBattleGame;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        NotifyWhenReadyResponse response = (NotifyWhenReadyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Notify from other player failed!");
        } else {
            multiPlayerSeaBattleGame.hasStarted = true;
            multiPlayerSeaBattleGame.isPlayersTurn = response.isPlayersTurn;
            application.notifyStartGame(response.playerNumber);
            application.showErrorMessage(response.isPlayersTurn ? "You start." : "Opponent starts.");
            client.addListener(OpponentFireShotResponse.class.getSimpleName(), new OpponentFireShotResponseListener(application, multiPlayerSeaBattleGame, client));
            client.removeListener(NotifyWhenReadyResponse.class.getSimpleName(), this);
        }
    }
}