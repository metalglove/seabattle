package seabattlegame.listeners;

import domain.ShotType;
import messaging.messages.responses.OpponentFireShotResponse;
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
        // TODO: verify if it works
        OpponentFireShotResponse response = (OpponentFireShotResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Opponent fired but server faulted! try again later.");
        } else {
            application.opponentFiresShot(response.firingPlayerNumber, response.shotType);
            if (response.shotType == ShotType.ALLSUNK) {
                client.removeListener(OpponentFireShotResponse.class.getSimpleName(), this);
            }
        }
    }
}