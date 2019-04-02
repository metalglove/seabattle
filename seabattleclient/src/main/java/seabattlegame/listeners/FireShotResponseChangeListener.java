package seabattlegame.listeners;

import domain.Point;
import domain.ShotType;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.FireShotResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FireShotResponseChangeListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public FireShotResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        FireShotResponse response = (FireShotResponse) evt.getNewValue();
        if (!response.success) {
            application.showErrorMessage("Fire shot failed!");
            messageLogger.error("Fire shot failed!");
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
                messageLogger.info("Player won");
                game.endGame();
            } else {
                game.setPlayerTurn(false);
            }
        }
        client.removeListener(FireShotResponse.class.getSimpleName(), this);
    }
}