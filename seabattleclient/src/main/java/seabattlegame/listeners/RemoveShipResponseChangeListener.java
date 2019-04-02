package seabattlegame.listeners;

import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.RemoveShipResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final int playerNr;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public RemoveShipResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNr, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.playerNr = playerNr;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveShipResponse response = (RemoveShipResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to remove ship from proposed point.");
            messageLogger.error("Failed to remove ship from proposed point.");
        } else {
            game.setHasPlacedAllShips(false);

            application.removeShip(playerNr, response.ship);
        }
        client.removeListener(RemoveShipResponse.class.getSimpleName(), this);
    }
}