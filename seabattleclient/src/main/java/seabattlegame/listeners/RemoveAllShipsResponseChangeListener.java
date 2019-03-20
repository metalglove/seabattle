package seabattlegame.listeners;

import messaging.messages.responses.RemoveAllShipsResponse;
import seabattlegame.Client;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveAllShipsResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final MultiPlayerSeaBattleGame multiPlayerSeaBattleGame;
    private final int playerNr;
    private final Client client;

    public RemoveAllShipsResponseChangeListener(ISeaBattleGUI application, MultiPlayerSeaBattleGame multiPlayerSeaBattleGame, int playerNr, Client client) {
        this.application = application;
        this.multiPlayerSeaBattleGame = multiPlayerSeaBattleGame;
        this.playerNr = playerNr;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveAllShipsResponse response = (RemoveAllShipsResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to remove all ships.");
        } else {
            multiPlayerSeaBattleGame.hasPlacedAllShips = false;
        }
        client.removeListener(RemoveAllShipsResponse.class.getSimpleName(), this);
    }
}