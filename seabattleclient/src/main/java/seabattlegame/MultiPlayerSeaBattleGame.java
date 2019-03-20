/*
 * Sea Battle Start project.
 */
package seabattlegame;

import domain.ShipType;
import messaging.messages.requests.*;
import messaging.messages.responses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seabattlegame.listeners.*;
import seabattlegui.ISeaBattleGUI;

import java.io.IOException;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class MultiPlayerSeaBattleGame implements ISeaBattleGame {

    private static final Logger log = LoggerFactory.getLogger(MultiPlayerSeaBattleGame.class);
    private Client client;
    private ISeaBattleGUI application;

    public MultiPlayerSeaBattleGame(ISeaBattleGUI application) {
        this.application = application;
        try {
            client = new Client("127.0.0.1", 9999);
            client.connect();
            client.startReading();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerPlayer(String name, String password) {
        log.debug("Register Player {} - password {} - multiplayermode", name, password);
        client.addListener(RegisterResponse.class.getSimpleName(), new RegisterResponseChangeListener(application, name, client));
        client.startWriting(new RegisterRequest(name, password));
    }

    @Override
    public void placeShipsAutomatically(int playerNr) {
        log.debug("placeShipsAutomatically with player number: {}", playerNr);
        client.addListener(PlaceShipsAutomaticallyResponse.class.getSimpleName(), new PlaceShipsAutomaticallyResponseChangeListener(application, playerNr, client));
        client.startWriting(new PlaceShipsAutomaticallyRequest(playerNr));
    }

    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        log.debug("placeShip with player number: {} - shipType: {} - bowX: {} - bowY: {} - horizontal: {}", playerNr, shipType, bowX, bowY, horizontal);
        client.addListener(PlaceShipResponse.class.getSimpleName(), new PlaceShipResponseChangeListener(application, playerNr, client));
        client.startWriting(new PlaceShipRequest(playerNr, shipType, bowX, bowY, horizontal));
    }

    @Override
    public void removeShip(int playerNr, int posX, int posY) {
        log.debug("removeShip with player number: {} - posX: {} - posY: {}", playerNr, posX, posY);
        client.addListener(RemoveShipResponse.class.getSimpleName(), new RemoveShipResponseChangeListener(application, playerNr, client));
        client.startWriting(new RemoveShipRequest(playerNr, posX, posY));
    }

    @Override
    public void removeAllShips(int playerNr) {
        log.debug("removeAllShips with player number: {}", playerNr);
        client.addListener(RemoveAllShipsResponse.class.getSimpleName(), new RemoveAllShipsResponseChangeListener(application, playerNr, client));
        client.startWriting(new RemoveAllShipsRequest(playerNr));
    }

    @Override
    public void notifyWhenReady(int playerNr) {
        log.debug("notifyWhenReady with player number: {}", playerNr);
        // TODO: check if player has placed all ships before setting to ready!
        client.addListener(NotifyWhenReadyResponse.class.getSimpleName(), new NotifyWhenReadyResponseChangeListener(application, playerNr, client));
        client.startWriting(new NotifyWhenReadyRequest(playerNr));
    }

    @Override
    public void fireShot(int playerNr, int posX, int posY) {
        log.debug("fireShot with player number: {} - posX: {} - posY: {}", playerNr, posX, posY);
        // TODO: check if it is the players turn!
        client.addListener(FireShotResponse.class.getSimpleName(), new FireShotResponseChangeListener(application, playerNr, client));
        client.startWriting(new FireShotRequest(playerNr, posX, posY));
    }

    @Override
    public void startNewGame(int playerNr) {
        log.debug("startNewGame with player number: {} ", playerNr);
        // TODO: check if the game is over
        client.addListener(StartNewGameResponse.class.getSimpleName(), new StartNewGameResponseChangeListener(application, playerNr, client));
        client.startWriting(new StartNewGameRequest(playerNr));
    }
}
