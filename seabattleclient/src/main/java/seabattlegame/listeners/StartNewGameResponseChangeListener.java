package seabattlegame.listeners;

import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.StartNewGameResponse;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StartNewGameResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final String name;
    private final Client client;
    private final ISeaBattleGame game;

    public StartNewGameResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, String name, Client client) {
        this.application = application;
        this.name = name;
        this.client = client;
        this.game = game;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StartNewGameResponse response = (StartNewGameResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Starting a new game failed!");
        } else {
            game.resetGame();
            application.setPlayerNumber(response.playerNumber, name);
            if (response.opponentName != null && response.opponentPlayerNumber != null) {
                application.setOpponentName(response.opponentPlayerNumber, response.opponentName);
            } else {
                client.addListener(OpponentRegisterResponse.class.getSimpleName(), new OpponentRegisterResponseListener(application, client));
            }
        }
        client.removeListener(StartNewGameResponse.class.getSimpleName(), this);
    }
}