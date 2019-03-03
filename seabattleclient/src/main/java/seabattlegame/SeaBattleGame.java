/*
 * Sea Battle Start project.
 */
package seabattlegame;

import messaging.messages.commands.RegisterCommand;
import messaging.messages.requests.PlayerNumberRequest;
import messaging.messages.responses.PlayerNumberResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seabattlegame.listeners.PlayerNumberResponseChangeListener;
import seabattlegui.ISeaBattleGUI;
import seabattlegui.ShipType;

import java.io.IOException;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class SeaBattleGame implements ISeaBattleGame {

  private static final Logger log = LoggerFactory.getLogger(SeaBattleGame.class);
  private Client client;

  public SeaBattleGame() {
    try {
      client = new Client("127.0.0.1", 9999);
      client.connect();
      client.startReading();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void registerPlayer(String name, String password, ISeaBattleGUI application, boolean singlePlayerMode) {
    log.debug("Register Player {} - password {}", name, password);
    client.startWriting(new RegisterCommand(name, password, singlePlayerMode));
    client.addListener(PlayerNumberResponse.class.getName(), new PlayerNumberResponseChangeListener(application, name, client));
    client.startWriting(new PlayerNumberRequest(name));
  }

  @Override
  public void placeShipsAutomatically(int playerNr) {
    throw new UnsupportedOperationException("Method placeShipsAutomatically() not implemented.");
  }

  @Override
  public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
    throw new UnsupportedOperationException("Method placeShip() not implemented.");
  }

  @Override
  public void removeShip(int playerNr, int posX, int posY) {
    throw new UnsupportedOperationException("Method removeShip() not implemented.");
  }

  @Override
  public void removeAllShips(int playerNr) {
    throw new UnsupportedOperationException("Method removeAllShips() not implemented.");
  }

  @Override
  public void notifyWhenReady(int playerNr) {
    throw new UnsupportedOperationException("Method notifyWhenReady() not implemented.");
  }

  @Override
  public void fireShot(int playerNr, int posX, int posY) {
    throw new UnsupportedOperationException("Method fireShot() not implemented.");
  }

  @Override
  public void startNewGame(int playerNr) {
    throw new UnsupportedOperationException("Method startNewGame() not implemented.");
  }
}
