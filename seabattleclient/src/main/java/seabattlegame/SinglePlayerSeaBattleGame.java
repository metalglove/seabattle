package seabattlegame;

import domain.ShipType;
import messaging.messages.requests.FireShotRequest;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.RegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seabattlegame.listeners.RegisterResponseChangeListener;
import seabattlegui.ISeaBattleGUI;

public class SinglePlayerSeaBattleGame implements ISeaBattleGame {

    private static final Logger log = LoggerFactory.getLogger(SinglePlayerSeaBattleGame.class);
    private ISeaBattleGUI application;
    private Client client;
    public boolean hasPlacedAllShips = false;
    public boolean isReady = false;
    public boolean hasStarted = false;
    public boolean isPlayersTurn = false;
    public boolean hasGameEnded = false;

    public SinglePlayerSeaBattleGame(ISeaBattleGUI application) {
        this.application = application;
    }

    @Override
    public void registerPlayer(String name, String password) {
        log.debug("Register Player {} - password {} - singleplayermode", name, password);

        client.addListener(RegisterResponse.class.getSimpleName(), new RegisterResponseChangeListener(application, name, this, client));
        client.startWriting(new RegisterRequest(name, password));
    }

    @Override
    public void placeShipsAutomatically(int playerNr) {

    }

    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {

    }

    @Override
    public void removeShip(int playerNr, int posX, int posY) {

    }

    @Override
    public void removeAllShips(int playerNr) {

    }

    @Override
    public void notifyWhenReady(int playerNr) {

    }

    @Override
    public void fireShot(int playerNr, int posX, int posY) {
        new FireShotRequest(1, posX, posY);
    }

    @Override
    public void startNewGame(int playerNr) {

    }

    @Override
    public void resetGame() {
        hasPlacedAllShips = false;
        isReady = false;
        hasStarted = false;
        isPlayersTurn = false;
        hasGameEnded = false;
    }

    @Override
    public void endGame() {
        hasGameEnded = true;
    }

    @Override
    public void setHasPlacedAllShips(boolean value) {
        hasPlacedAllShips = value;
    }

    @Override
    public void setPlayerTurn(boolean value) {
        isPlayersTurn = value;
    }

    @Override
    public void setStarted(boolean value) {
        hasStarted = value;
    }

    @Override
    public void setPlayerName(String name) {

    }
}
