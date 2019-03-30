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
import seabattlegui.SquareState;

import java.io.IOException;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class SeaBattleGame implements ISeaBattleGame {

    private static final Logger log = LoggerFactory.getLogger(SeaBattleGame.class);
    private final Client client;
    private final ISeaBattleGUI application;
    private boolean hasPlacedAllShips = false;
    private boolean isReady = false;
    private boolean hasStarted = false;
    private boolean isPlayersTurn = false;
    private boolean hasGameEnded = false;
    private String playerName;

    public SeaBattleGame(ISeaBattleGUI application) throws IOException {
        this.application = application;
        try {
            client = new Client("127.0.0.1", 9999);
            client.connect();
            client.startReading();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void registerPlayer(String name, String password, boolean multiPlayer) {
        log.debug("Register Player {} - password {} - Mode {}", name, password, multiPlayer ? "Multi-Player" : "Single-Player");
        if ("CPU".equals(name)) {
            application.showErrorMessage("You are not allowed to be named CPU, this is reserved for the AI in SinglePlayer Mode.");
            return;
        }
        client.addListener(RegisterResponse.class.getSimpleName(), new RegisterResponseChangeListener(application, name, this, client));
        client.startWriting(new RegisterRequest(name, password, multiPlayer));
    }

    @Override
    public void placeShipsAutomatically(int playerNr) {
        log.debug("placeShipsAutomatically with player number: {}", playerNr);
        if (isReady) {
            application.showErrorMessage("You are not allowed to change your ships after readying up.");
            return;
        }
        client.addListener(PlaceShipsAutomaticallyResponse.class.getSimpleName(), new PlaceShipsAutomaticallyResponseChangeListener(application, this, playerNr, client));
        client.startWriting(new PlaceShipsAutomaticallyRequest(playerNr));
    }

    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        log.debug("placeShip with player number: {} - shipType: {} - bowX: {} - bowY: {} - horizontal: {}", playerNr, shipType, bowX, bowY, horizontal);
        if (isReady) {
            application.showErrorMessage("You are not allowed to change your ships after readying up.");
            return;
        }
        client.addListener(PlaceShipResponse.class.getSimpleName(), new PlaceShipResponseChangeListener(application, this, playerNr, client));
        client.startWriting(new PlaceShipRequest(playerNr, shipType, bowX, bowY, horizontal));
    }

    @Override
    public void removeShip(int playerNr, int posX, int posY) {
        log.debug("removeShip with player number: {} - posX: {} - posY: {}", playerNr, posX, posY);
        if (isReady) {
            application.showErrorMessage("You are not allowed to change your ships after readying up.");
            return;
        }
        client.addListener(RemoveShipResponse.class.getSimpleName(), new RemoveShipResponseChangeListener(application, this, playerNr, client));
        client.startWriting(new RemoveShipRequest(playerNr, posX, posY));
    }

    @Override
    public void removeAllShips(int playerNr) {
        log.debug("removeAllShips with player number: {}", playerNr);
        if (isReady) {
            application.showErrorMessage("You are not allowed to change your ships after readying up.");
            return;
        }
        client.addListener(RemoveAllShipsResponse.class.getSimpleName(), new RemoveAllShipsResponseChangeListener(application, this, playerNr, client));
        client.startWriting(new RemoveAllShipsRequest(playerNr));
    }

    @Override
    public void notifyWhenReady(int playerNr) {
        log.debug("notifyWhenReady with player number: {}", playerNr);
        if (isReady) {
            application.showErrorMessage("You are already ready.");
            return;
        }
        if (!hasPlacedAllShips) {
            application.showErrorMessage("You have not yet placed all your ships.");
            return;
        }
        isReady = true;
        client.addListener(NotifyWhenReadyResponse.class.getSimpleName(), new NotifyWhenReadyResponseChangeListener(application, this, client));
        client.startWriting(new NotifyWhenReadyRequest(playerNr));
    }

    @Override
    public void fireShot(int playerNr, int posX, int posY) {
        log.debug("fireShot with player number: {} - posX: {} - posY: {}", playerNr, posX, posY);
        if (!hasStarted) {
            application.showErrorMessage("The game has not yet started.");
            return;
        }
        if (!isPlayersTurn) {
            application.showErrorMessage("It is not your turn yet.");
            application.showSquareOpponent(playerNr, posX, posY, SquareState.WATER);
            return;
        }
        client.addListener(FireShotResponse.class.getSimpleName(), new FireShotResponseChangeListener(application, this, client));
        client.startWriting(new FireShotRequest(playerNr, posX, posY));
    }

    @Override
    public void startNewGame(int playerNr, boolean multiPlayer) {
        log.debug("startNewGame with player number: {} ", playerNr);
        if (!hasGameEnded) {
            application.showErrorMessage("Game has not ended yet!");
            return;
        }
        client.addListener(StartNewGameResponse.class.getSimpleName(), new StartNewGameResponseChangeListener(application,this , playerName, client));
        client.startWriting(new StartNewGameRequest(playerNr, multiPlayer));
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
        playerName = name;
    }
}
