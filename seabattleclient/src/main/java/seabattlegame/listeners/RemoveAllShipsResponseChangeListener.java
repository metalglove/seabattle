package seabattlegame.listeners;

import domain.Ship;
import messaging.messages.responses.RemoveAllShipsResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveAllShipsResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNr;
    private final Client client;
    private final MessageLogger messageLogger;

    public RemoveAllShipsResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNr, Client client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.playerNr = playerNr;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveAllShipsResponse response = (RemoveAllShipsResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to remove all ships.");
            messageLogger.error("Failed to remove all ships.");
        } else {
            for(Ship ship : response.removedShips){
                application.removeShip(playerNr, ship);
            }
            game.setHasPlacedAllShips(false);
        }
        client.removeListener(RemoveAllShipsResponse.class.getSimpleName(), this);
    }
}