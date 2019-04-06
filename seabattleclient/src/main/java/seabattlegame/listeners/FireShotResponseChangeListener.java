package seabattlegame.listeners;

import common.MessageLogger;
import domain.Point;
import domain.ShotType;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.FireShotResponse;
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
        if (!response.isSuccess()) {
            application.showErrorMessage("Fire shot failed!");
            messageLogger.error("Fire shot failed!");
        } else {
            if (response.getShip() != null) {
                for (Point point : response.getShip().getPointsHit()) {
                    application.playerFiresShot(response.getFiringPlayerNumber(), response.getShotType(), point);
                }
            } else {
                application.playerFiresShot(response.getFiringPlayerNumber(), response.getShotType(), response.getPoint());
            }
            if (response.getShotType() == ShotType.ALLSUNK) {
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