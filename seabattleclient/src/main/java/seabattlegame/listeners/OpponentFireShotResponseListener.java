package seabattlegame.listeners;

import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OpponentFireShotResponseListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNumber;
    private final Client client;

    public OpponentFireShotResponseListener(ISeaBattleGUI application, int playerNumber, Client client) {
        this.application = application;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: implement
        client.removeListener(this);
    }
}