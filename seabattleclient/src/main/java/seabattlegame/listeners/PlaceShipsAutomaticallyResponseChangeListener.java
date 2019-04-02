package seabattlegame.listeners;

import domain.Ship;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlaceShipsAutomaticallyResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNumber;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public PlaceShipsAutomaticallyResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNumber, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.playerNumber = playerNumber;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlaceShipsAutomaticallyResponse response = (PlaceShipsAutomaticallyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to automatically place all ships!");
            messageLogger.error("Failed to automatically place all ships!");
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