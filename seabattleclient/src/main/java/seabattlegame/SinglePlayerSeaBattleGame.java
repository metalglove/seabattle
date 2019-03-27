package seabattlegame;

import domain.ShipType;
import messaging.messages.requests.FireShotRequest;
import messaging.messages.requests.PlaceShipsAutomaticallyRequest;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.messages.responses.RegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seabattlegame.listeners.PlaceShipsAutomaticallyResponseChangeListener;
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

        client.addListener(RegisterResponse.class.getSimpleName(), new RegisterResponseChangeListener(application, name, client));
        client.startWriting(new RegisterRequest(name, password));
    }

    @Override
    public void placeShipsAutomatically(int playerNr) {
//        log.debug("placeShipsAutomatically with player number: {}", playerNr);
//        if (isReady) {
//            application.showErrorMessage("You are not allowed to change your ships after readying up.");
//            return;
//        }
//        client.addListener(PlaceShipsAutomaticallyResponse.class.getSimpleName(), new PlaceShipsAutomaticallyResponseChangeListener(application, this, playerNr, client));
//        client.startWriting(new PlaceShipsAutomaticallyRequest(playerNr));
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
}
