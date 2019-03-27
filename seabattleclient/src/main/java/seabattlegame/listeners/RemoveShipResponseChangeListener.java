package seabattlegame.listeners;

import domain.Ship;
import messaging.messages.responses.RemoveShipResponse;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNr;
    private final Client client;

    public RemoveShipResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNr, Client client) {
        this.application = application;
        this.game = game;
        this.playerNr = playerNr;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveShipResponse response = (RemoveShipResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to remove ship from proposed point.");
        } else {
            game.setHasPlacedAllShips(false);

            application.removeShip(playerNr, response.ship);
        }
        client.removeListener(RemoveShipResponse.class.getSimpleName(), this);
    }
}