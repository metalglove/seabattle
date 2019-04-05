package seabattlegame.listeners;

import domain.Point;
import domain.ShotType;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.OpponentFireShotResponse;
import messaging.utilities.MessageLogger;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OpponentFireShotResponseListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ISeaBattleGame game;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public OpponentFireShotResponseListener(ISeaBattleGUI application, ISeaBattleGame game, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.game = game;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        OpponentFireShotResponse response = (OpponentFireShotResponse) evt.getNewValue();
        if (!response.isSuccess()) {
            application.showErrorMessage("Opponent fired but server faulted! try again later.");
            messageLogger.error("Opponent fired but server faulted!");
        } else {
            if (response.getShip() != null) {
                for (Point point : response.getShip().getPointsHit()) {
                    application.opponentFiresShot(response.getFiringPlayerNumber(), response.getShotType(), point);
                }
            } else {
                application.opponentFiresShot(response.getFiringPlayerNumber(), response.getShotType(), response.getPoint());
            }
            if (response.getShotType() == ShotType.ALLSUNK) {
                application.showErrorMessage("You lost!");
                messageLogger.info("Player lost.");
                game.endGame();
                client.removeListener(OpponentFireShotResponse.class.getSimpleName(), this);
            } else {
                game.setPlayerTurn(true);
            }
        }
    }
}