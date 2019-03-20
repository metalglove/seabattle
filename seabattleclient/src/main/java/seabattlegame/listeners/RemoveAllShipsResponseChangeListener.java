package seabattlegame.listeners;

import messaging.messages.responses.RemoveAllShipsResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RemoveAllShipsResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNr;
    private final Client client;

    public RemoveAllShipsResponseChangeListener(ISeaBattleGUI application, int playerNr, Client client) {
        this.application = application;
        this.playerNr = playerNr;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RemoveAllShipsResponse response = (RemoveAllShipsResponse) evt.getNewValue();
        if (!response.success) {
            System.out.println("Failed to remove all ships.");
        } else {
            System.out.println("Successfully removed all ships.");
        }
        client.removeListener(RemoveAllShipsResponse.class.getSimpleName(), this);
    }
}