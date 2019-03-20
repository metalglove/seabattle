package seabattlegame.listeners;

import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.RegisterResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegisterResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final String name;
    private final Client client;

    public RegisterResponseChangeListener(ISeaBattleGUI application, String name, Client client) {
        this.application = application;
        this.name = name;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RegisterResponse response = (RegisterResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Failed to register!");
        } else {
            application.setPlayerNumber(response.playerNumber, name);
            if (response.opponentName != null && response.opponentPlayerNumber != null) {
                application.setOpponentName(response.opponentPlayerNumber, response.opponentName);
            } else {
                client.addListener(OpponentRegisterResponse.class.getSimpleName(), new OpponentRegisterResponseListener(application, client));
            }
        }
        client.removeListener(RegisterResponse.class.getSimpleName(), this);
    }
}