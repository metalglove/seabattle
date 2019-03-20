package seabattlegame.listeners;

import messaging.messages.responses.RemoveShipResponse;
import seabattlegame.Client;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final MultiPlayerSeaBattleGame multiPlayerSeaBattleGame;
    private final int playerNr;
    private final Client client;

    public RemoveShipResponseChangeListener(ISeaBattleGUI application, MultiPlayerSeaBattleGame multiPlayerSeaBattleGame, int playerNr, Client client) {
        this.application = application;
        this.multiPlayerSeaBattleGame = multiPlayerSeaBattleGame;
        this.playerNr = playerNr;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveShipResponse response = (RemoveShipResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to remove ship from proposed point.");
        } else {
            multiPlayerSeaBattleGame.hasPlacedAllShips = false;
        }
        client.removeListener(RemoveShipResponse.class.getSimpleName(), this);
    }
}