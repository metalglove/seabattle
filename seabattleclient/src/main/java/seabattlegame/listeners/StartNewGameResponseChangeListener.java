package seabattlegame.listeners;

import messaging.messages.responses.NotifyWhenReadyResponse;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StartNewGameResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final int playerNumber;
    private final Client client;
    private final ISeaBattleGame game;

    public StartNewGameResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, int playerNumber, Client client) {
        this.application = application;
        this.playerNumber = playerNumber;
        this.client = client;
        this.game = game;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        NotifyWhenReadyResponse response = (NotifyWhenReadyResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("New game!"); // TODO: fix
        } else {
            game.resetGame();
            application.notifyStartGame(response.playerNumber);
        }
        client.removeListener(NotifyWhenReadyResponse.class.getSimpleName(), this);
    }
}