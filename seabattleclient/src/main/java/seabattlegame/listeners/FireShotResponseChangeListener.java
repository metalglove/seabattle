package seabattlegame.listeners;

import domain.Point;
import domain.ShotType;
import messaging.messages.responses.FireShotResponse;
import seabattlegame.Client;
import seabattlegame.MultiPlayerSeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FireShotResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final MultiPlayerSeaBattleGame multiPlayerSeaBattleGame;
    private final Client client;

    public FireShotResponseChangeListener(ISeaBattleGUI application, MultiPlayerSeaBattleGame multiPlayerSeaBattleGame, Client client) {
        this.application = application;
        this.multiPlayerSeaBattleGame = multiPlayerSeaBattleGame;
        this.client = client;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        FireShotResponse response = (FireShotResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Fire shot failed!");
        } else {
            if (response.ship != null) {
                for (Point point : response.ship.getPointsHit()) {
                    application.playerFiresShot(response.firingPlayerNumber, response.shotType, point);
                }
            } else {
                application.playerFiresShot(response.firingPlayerNumber, response.shotType, response.point);
            }
            if (response.shotType == ShotType.ALLSUNK) {
                application.showErrorMessage("You won!");
            } else {
                multiPlayerSeaBattleGame.isPlayersTurn = false;
            }
        }
        client.removeListener(FireShotResponse.class.getSimpleName(), this);
    }
}