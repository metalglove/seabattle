package seabattlegame.listeners;

import messaging.messages.responses.PlaceShipResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlaceShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNumber;
    private final Client client;
    private final MessageLogger messageLogger;

    public PlaceShipResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNumber, Client client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.playerNumber = playerNumber;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlaceShipResponse response = (PlaceShipResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to place the ship!");
            messageLogger.error("Failed to place the ship!");
        } else {
            if (response.shipToRemove != null) {
                application.removeShip(playerNumber, response.shipToRemove);
            }
            if (response.hasPlacedAllShips) {
                game.setHasPlacedAllShips(true);
            }
            application.placeShip(playerNumber, response.ship);
        }
        client.removeListener(PlaceShipResponse.class.getSimpleName(), this);
    }
}