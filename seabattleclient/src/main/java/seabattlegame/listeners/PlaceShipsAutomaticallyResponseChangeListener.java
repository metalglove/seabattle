package seabattlegame.listeners;

import domain.Ship;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlaceShipsAutomaticallyResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNumber;
    private final Client client;

    public PlaceShipsAutomaticallyResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNumber, Client client) {
        this.application = application;
        this.game = game;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlaceShipsAutomaticallyResponse response = (PlaceShipsAutomaticallyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to automatically place all ships!");
        } else {
            for (Ship ship : response.shipsToRemove) {
                application.removeShip(playerNumber, ship);
            }
            for (Ship ship : response.shipsToAdd) {
                application.placeShip(playerNumber, ship);
            }
            game.setHasPlacedAllShips(true);
        }
        client.removeListener(PlaceShipsAutomaticallyResponse.class.getSimpleName(), this);
    }
}