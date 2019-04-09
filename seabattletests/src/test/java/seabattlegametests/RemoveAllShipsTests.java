package seabattlegametests;

import domain.Point;
import domain.Ship;
import domain.ships.*;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.messages.responses.RegisterResponse;
import messaging.messages.responses.RemoveAllShipsResponse;
import mocks.MockClient;
import mocks.MockSeaBattleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattlegui.SquareState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Remove all ships that are placed. The state of the ocean area in the
 * player's application will be kept up-to-date by method calls of
 * showSquarePlayer().
 * param playerNr  identification of player for which ships will be removed
 */
public class RemoveAllShipsTests {
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
    game.registerPlayer("Henk", "Karel32", false);
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
  public void should_Remove_All_Ships_When_Ships_Have_Previously_Been_Placed() {
    // Arrange
    game.placeShipsAutomatically(1);
    client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));

    // Act
    game.removeAllShips(1);
    client.setMockUpResponse(new RemoveAllShipsResponse(1, ships, true));

    // Assert
    assertEquals(0, application.numberSquaresPlayerWithSquareState(SquareState.SHIP));
  }

  @Test
  public void should_Set_ErrorMessage_When_Player_Is_Already_Ready() {
    // Arrange
    game.placeShipsAutomatically(1);
    client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));
    game.notifyWhenReady(1);
    client.setMockUpResponse(new NotifyWhenReadyResponse(1, true, true));

    // Act
    game.removeAllShips(1);

    // Assert
    assertEquals("You are not allowed to change your ships after readying up.", application.getErrorMessage());
  }

  @Test
  public void should_Set_ErrorMessage_When_Success_Is_False() {
    // Arrange
    game.placeShipsAutomatically(1);
    client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));

    // Act
    game.removeAllShips(1);
    client.setMockUpResponse(new RemoveAllShipsResponse(null, null, false));

    // Assert
    assertEquals("Failed to remove all ships.", application.getErrorMessage());
  }
}
