/*
 * Sea Battle Start project.
 */
package seabattlegame;

import common.MessageLogger;
import domain.ShipType;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.requests.*;
import messaging.messages.responses.*;
import seabattlegame.listeners.*;
import seabattlegui.ISeaBattleGUI;
import seabattlegui.SquareState;

import java.util.Objects;

import static java.lang.String.format;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class SeaBattleGame implements ISeaBattleGame {

  private static final MessageLogger gameMessageLogger = new MessageLogger("GAME");
  private static final MessageLogger handlerMessageLogger = new MessageLogger("RESPONSE-LISTENER");
  private final ObservableClientSocket client;
  private final ISeaBattleGUI application;
  private boolean hasPlacedAllShips = false;
  private boolean isReady = false;
  private boolean hasStarted = false;
  private boolean isPlayersTurn = false;
  private boolean hasGameEnded = false;
  private String playerName;

  public SeaBattleGame(ISeaBattleGUI application, ObservableClientSocket observableClientSocket) {
    this.application = application;
    this.client = observableClientSocket;
    client.startReading();
  }

  @Override
  public void registerPlayer(String name, String password, boolean multiPlayer) {
    if (Objects.isNull(name) || Objects.isNull(password) || Objects.isNull(multiPlayer))
      throw new IllegalArgumentException("Parameters can not be null");
    if (name.isBlank())
      throw new IllegalArgumentException("Name may not be blank.");
    if (password.isBlank())
      throw new IllegalArgumentException("Password may not be blank.");

    gameMessageLogger.info(format("Register Player %s - password %s - Mode %s", name, password, multiPlayer ? "Multi-Player" : "Single-Player"));
    if ("CPU".equals(name)) {
      application.showErrorMessage("You are not allowed to be named CPU, this is reserved for the AI in SinglePlayer Mode.");
      return;
    }
    client.addListener(RegisterResponse.class.getSimpleName(), new RegisterResponseChangeListener(application, name, this, client, handlerMessageLogger));
    client.addListener(ErrorResponse.class.getSimpleName(), new ErrorResponseChangeListener(application, this, client, handlerMessageLogger));
    client.startWriting(new RegisterRequest(name, password, multiPlayer));
  }

  @Override
  public void placeShipsAutomatically(int playerNr) {
    gameMessageLogger.info(format("PlaceShipsAutomatically with player number: %s", playerNr));
    if (isReady) {
      application.showErrorMessage("You are not allowed to change your ships after readying up.");
      return;
    }
    client.addListener(PlaceShipsAutomaticallyResponse.class.getSimpleName(), new PlaceShipsAutomaticallyResponseChangeListener(application, this, playerNr, client, handlerMessageLogger));
    client.startWriting(new PlaceShipsAutomaticallyRequest(playerNr));
  }

  @Override
  public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
    gameMessageLogger.info(format("PlaceShip with player number: %s - shipType: %s - bowX: %s - bowY: %s - horizontal: %s", playerNr, shipType, bowX, bowY, horizontal));
    if (isReady) {
      application.showErrorMessage("You are not allowed to change your ships after readying up.");
      return;
    }
    client.addListener(PlaceShipResponse.class.getSimpleName(), new PlaceShipResponseChangeListener(application, this, playerNr, client, handlerMessageLogger));
    client.startWriting(new PlaceShipRequest(playerNr, shipType, bowX, bowY, horizontal));
  }

  @Override
  public void removeShip(int playerNr, int posX, int posY) {
    gameMessageLogger.info(format("RemoveShip with player number: %s - posX: %s - posY: %s", playerNr, posX, posY));
    if (isReady) {
      application.showErrorMessage("You are not allowed to change your ships after readying up.");
      return;
    }
    client.addListener(RemoveShipResponse.class.getSimpleName(), new RemoveShipResponseChangeListener(application, this, playerNr, client, handlerMessageLogger));
    client.startWriting(new RemoveShipRequest(playerNr, posX, posY));
  }

  @Override
  public void removeAllShips(int playerNr) {
    gameMessageLogger.info(format("RemoveAllShips with player number: %s", playerNr));
    if (isReady) {
      application.showErrorMessage("You are not allowed to change your ships after readying up.");
      return;
    }
    client.addListener(RemoveAllShipsResponse.class.getSimpleName(), new RemoveAllShipsResponseChangeListener(application, this, playerNr, client, handlerMessageLogger));
    client.startWriting(new RemoveAllShipsRequest(playerNr));
  }

  @Override
  public void notifyWhenReady(int playerNr) {
    gameMessageLogger.info(format("NotifyWhenReady with player number: %s", playerNr));
    if (isReady) {
      application.showErrorMessage("You are already ready.");
      return;
    }
    if (!hasPlacedAllShips) {
      application.showErrorMessage("You have not yet placed all your ships.");
      return;
    }
    isReady = true;
    client.addListener(NotifyWhenReadyResponse.class.getSimpleName(), new NotifyWhenReadyResponseChangeListener(application, this, client, handlerMessageLogger));
    client.startWriting(new NotifyWhenReadyRequest(playerNr));
  }

  @Override
  public void fireShot(int playerNr, int posX, int posY) {
    gameMessageLogger.info(format("FireShot with player number: %s - posX: %s - posY: %s", playerNr, posX, posY));
    if (!hasStarted) {
      application.showErrorMessage("The game has not yet started.");
      return;
    }
    if (!isPlayersTurn) {
      application.showErrorMessage("It is not your turn yet.");
      application.showSquareOpponent(playerNr, posX, posY, SquareState.WATER);
      return;
    }
    if (posX < 0 || posX > 9 || posY < 0 || posY > 9) {
      application.showErrorMessage("Position is out of range!");
      return;
    }
    client.addListener(FireShotResponse.class.getSimpleName(), new FireShotResponseChangeListener(application, this, client, handlerMessageLogger));
    client.startWriting(new FireShotRequest(playerNr, posX, posY));
  }

  @Override
  public void startNewGame(int playerNr, boolean multiPlayer) {
    gameMessageLogger.info(format("StartNewGame with player number: %s", playerNr));
    if (!hasGameEnded) {
      application.showErrorMessage("Game has not ended yet!");
      return;
    }
    client.addListener(StartNewGameResponse.class.getSimpleName(), new StartNewGameResponseChangeListener(application, this, playerName, client, handlerMessageLogger));
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
