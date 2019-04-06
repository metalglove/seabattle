package seabattlegame.listeners;

import common.MessageLogger;
import domain.Ship;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.RemoveAllShipsResponse;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveAllShipsResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNr;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public RemoveAllShipsResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNr, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.playerNr = playerNr;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveAllShipsResponse response = (RemoveAllShipsResponse) evt.getNewValue();
        if (!response.isSuccess()) {
            application.showErrorMessage("Failed to remove all ships.");
            messageLogger.error("Failed to remove all ships.");
        } else {
            for(Ship ship : response.getRemovedShips()){
                application.removeShip(playerNr, ship);
            }
            game.setHasPlacedAllShips(false);
        }
        client.removeListener(RemoveAllShipsResponse.class.getSimpleName(), this);
    }
}