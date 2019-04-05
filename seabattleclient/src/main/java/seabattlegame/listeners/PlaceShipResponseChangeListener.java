package seabattlegame.listeners;

import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.PlaceShipResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlaceShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNumber;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public PlaceShipResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNumber, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.playerNumber = playerNumber;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlaceShipResponse response = (PlaceShipResponse) evt.getNewValue();
        if (!response.isSuccess()) {
            application.showErrorMessage("Failed to place the ship!");
            messageLogger.error("Failed to place the ship!");
        } else {
            if (response.getShipToRemove() != null) {
                application.removeShip(playerNumber, response.getShipToRemove());
            }
            if (response.hasPlacedAllShips()) {
                game.setHasPlacedAllShips(true);
            }
            application.placeShip(playerNumber, response.getShip());
        }
        client.removeListener(PlaceShipResponse.class.getSimpleName(), this);
    }
}