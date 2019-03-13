package seabattlegame.listeners;

import messaging.messages.responses.PlayerNumberResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerNumberResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final String name;
    private final Client client;

    public PlayerNumberResponseChangeListener(ISeaBattleGUI application, String name, Client client) {
        this.application = application;
        this.name = name;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerNumberResponse response = (PlayerNumberResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("No player registered with that name!");
        } else {
            application.setPlayerNumber(response.playerNumber, name);
        }
        client.removeListener(this);
    }
}
