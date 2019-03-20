package seabattlegame.listeners;

import messaging.messages.responses.NotifyWhenReadyResponse;
import seabattlegame.Client;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StartNewGameResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNumber;
    private final Client client;

    public StartNewGameResponseChangeListener(ISeaBattleGUI application, int playerNumber, Client client) {
        this.application = application;
        this.playerNumber = playerNumber;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        NotifyWhenReadyResponse response = (NotifyWhenReadyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("New game!"); // TODO: fix
        } else {
            application.notifyStartGame(response.playerNumber);
            // TODO: reset every bool in multiplayergame
        }
        client.removeListener(NotifyWhenReadyResponse.class.getSimpleName(), this);
    }
}