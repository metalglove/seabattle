package seabattlegame.listeners;

import messaging.messages.responses.PlaceShipResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlaceShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNumber;
    private final Client client;

    public PlaceShipResponseChangeListener(ISeaBattleGUI application, int playerNumber, Client client) {
        this.application = application;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlaceShipResponse response = (PlaceShipResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to place the ship!");
        } else {
            if (response.shipToRemove != null) {
                application.removeShip(playerNumber, response.shipToRemove);
            }
            application.placeShip(playerNumber, response.ship);
        }
        client.removeListener(PlaceShipResponse.class.getSimpleName(), this);
    }
}