package seabattlegame.listeners;

import messaging.messages.responses.RemoveShipResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveShipResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNr;
    private final Client client;

    public RemoveShipResponseChangeListener(ISeaBattleGUI application, int playerNr, Client client) {
        this.application = application;
        this.playerNr = playerNr;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveShipResponse response = (RemoveShipResponse) evt.getNewValue();
        if (!response.success) {
            System.out.println("Failed to remove ship from proposed point.");
        } else {
            System.out.println("Successfully removed ship from proposed point.");
        }
        client.removeListener(RemoveShipResponse.class.getSimpleName(), this);
    }
}