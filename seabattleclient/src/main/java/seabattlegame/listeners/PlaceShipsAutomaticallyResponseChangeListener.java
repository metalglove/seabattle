package seabattlegame.listeners;

import domain.Ship;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlaceShipsAutomaticallyResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNumber;
    private final Client client;

    public PlaceShipsAutomaticallyResponseChangeListener(ISeaBattleGUI application, int playerNumber, Client client) {
        this.application = application;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlaceShipsAutomaticallyResponse response = (PlaceShipsAutomaticallyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to automatically place all ships!");
        } else {
            for (Ship ship : response.ships) {
                application.placeShip(playerNumber, ship);
            }
        }
        client.removeListener(this);
    }
}