package seabattlegametests;

import domain.Point;
import domain.Ship;
import domain.ShipType;
import domain.ships.*;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.messages.responses.PlaceShipResponse;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.messages.responses.RegisterResponse;
import mocks.MockClient;
import mocks.MockSeaBattleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Notify that the player is ready to play the game, i.e., all ships have
 * been placed. When not all ships have been placed, the message
 * "You have not yet placed all your ships." will be sent to the player's application
 * by a method call of showErrorMessage().
 * param playerNr identification of player who is ready to play the game
 */
public class NotifyWhenReadyTests {
  private ISeaBattleGame game;
  private MockSeaBattleApplication application;
  private MockClient client;
  private List<Ship> ships;

  @SuppressWarnings("Duplicates")
  @BeforeEach
  public void setUp() {
    // Create the mock socket client
    client = new MockClient();

    // Create mock Sea Battle GUI for player
    application = new MockSeaBattleApplication();

    // Create the game
    game = new SeaBattleGame(application, client);

    game.registerPlayer("Mario", "password", false);
    client.setMockUpResponse(new RegisterResponse(1, true, -1, "CPU", duplicate));
    AircraftCarrier aircraftCarrier = new AircraftCarrier(new Point(1, 1), true);
    BattleShip battleShip = new BattleShip(new Point(1, 2), true);
    Cruiser cruiser = new Cruiser(new Point(1, 3), true);
    MineSweeper mineSweeper = new MineSweeper(new Point(1, 4), true);
    Submarine submarine = new Submarine(new Point(1, 5), true);
    ships = new ArrayList<>();
    ships.add(aircraftCarrier);
    ships.add(battleShip);
    ships.add(cruiser);
    ships.add(mineSweeper);
    ships.add(submarine);
  }

  @Test
  public void should_Notify_Player_When_All_Ships_Have_Been_Placed() {
    // Arrange
    game.placeShipsAutomatically(1);
    client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));

    // Act
    game.notifyWhenReady(1);
    client.setMockUpResponse(new NotifyWhenReadyResponse(1, true, true));

    // Assert
    assertTrue(application.isGameStarted());
  }

  @Test
  public void should_Set_ErrorMessage_When_Player_Is_Already_Ready() {
    // Arrange
    game.placeShipsAutomatically(1);
    client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));
    game.notifyWhenReady(1);
    client.setMockUpResponse(new NotifyWhenReadyResponse(1, true, true));

    // Act
    game.notifyWhenReady(1);

    // Assert
    assertEquals("You are already ready.", application.getErrorMessage());
  }

  @Test
  public void should_Not_Set_GameStarted_In_Application_When_Not_All_Ships_Have_Been_Placed_After_Calling_NotifyWhenReady() {
    // Arrange
    game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1, true);
    client.setMockUpResponse(new PlaceShipResponse(1, new AircraftCarrier(new Point(1, 1), true), true, null, false));

    // Act
    game.notifyWhenReady(1);

    // Assert
    String errorMessage = application.getErrorMessage();
    assertEquals("You have not yet placed all your ships.", errorMessage);
  }

  @Test()
  public void should_Set_ErrorMessage_When_Success_Is_False() {
    // Arrange
    game.placeShipsAutomatically(1);
    client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));

    // Act
    game.notifyWhenReady(1);
    client.setMockUpResponse(new NotifyWhenReadyResponse(null, false, true));

    // Assert
    assertEquals("Notify from other player failed!", application.getErrorMessage());
  }
}
