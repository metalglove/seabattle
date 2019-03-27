package seabattlegame.listeners;

import domain.Point;
import domain.ShotType;
import messaging.messages.responses.OpponentFireShotResponse;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OpponentFireShotResponseListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final Client client;

    public OpponentFireShotResponseListener(ISeaBattleGUI application, ISeaBattleGame game, Client client) {
        this.application = application;
        this.game = game;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: verify if it works
        OpponentFireShotResponse response = (OpponentFireShotResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Opponent fired but server faulted! try again later.");
        } else {
            if (response.ship != null) {
                for (Point point : response.ship.getPointsHit()) {
                    application.opponentFiresShot(response.firingPlayerNumber, response.shotType, point);
                }
            } else {
                application.opponentFiresShot(response.firingPlayerNumber, response.shotType, response.point);
            }
            if (response.shotType == ShotType.ALLSUNK) {
                application.showErrorMessage("You lost!");
                game.endGame();
                client.removeListener(OpponentFireShotResponse.class.getSimpleName(), this);
            } else {
                game.setPlayerTurn(true);
            }
        }
    }
}