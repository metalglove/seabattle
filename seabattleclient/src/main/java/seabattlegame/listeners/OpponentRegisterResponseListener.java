package seabattlegame.listeners;

import messaging.messages.responses.OpponentRegisterResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OpponentRegisterResponseListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final Client client;

    public OpponentRegisterResponseListener(ISeaBattleGUI application, Client client) {
        this.application = application;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        OpponentRegisterResponse response = (OpponentRegisterResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Opponent registered but server faulted!");
            // TODO: this should never happen but if it does....
        } else {
            application.setOpponentName(response.opponentNumber, response.opponentName);
            client.removeListener(OpponentRegisterResponse.class.getSimpleName(), this);
        }
    }
}