package seabattlegame.listeners;

import messaging.messages.responses.FireShotResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FireShotResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNumber;
    private final Client client;

    public FireShotResponseChangeListener(ISeaBattleGUI application, int playerNumber, Client client) {
        this.application = application;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        FireShotResponse response = (FireShotResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Fire shot failed!");
        } else {
            application.playerFiresShot(response.firingPlayerNumber, response.shotType);
        }
        client.removeListener(this);
    }
}