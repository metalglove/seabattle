package seabattlegametests;

import domain.Point;
import domain.Ship;
import domain.ShipType;
import domain.ships.*;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.messages.responses.PlaceShipResponse;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.messages.responses.RegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattlegui.SquareState;
import seabattleunittests.MockClient;
import seabattleunittests.MockSeaBattleApplication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Place ships automatically. Ships that are already placed will be removed.
 * All ships will be placed such that they fit entirely within the grid
 * and have no overlap with each other. The state of the ocean area in the
 * player's application will be kept up-to-date by method calls of
 * showSquarePlayer().
 * param playerNr identification of player for which ships will be placed
 */
public class PlaceShipsAutomaticallyTests {

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

        game.registerPlayer("Mario", "password",false);
        client.setMockUpResponse(new RegisterResponse(1, true, -1, "CPU"));
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
    public void should_Place_All_Ships_Successfully() {
        // Arrange & Act
        game.placeShipsAutomatically(1);
        client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));

        // Assert
        assertEquals(17, application.numberSquaresPlayerWithSquareState(SquareState.SHIP));
    }

    @Test
    public void should_Set_ErrorMessage_When_Player_Is_Already_Ready() {
        // Arrange
        game.placeShipsAutomatically(1);
        client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));

        game.notifyWhenReady(1);
        client.setMockUpResponse(new NotifyWhenReadyResponse(1, true, true));
        // Act
        game.placeShipsAutomatically(1);

        // Assert
        assertEquals("You are not allowed to change your ships after readying up.", application.getErrorMessage());
    }

    @Test
    public void should_Remove_All_Previous_Placed_Ships_Successfully() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 9,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new AircraftCarrier(new Point(1, 9), true), true,null, false));

        // Act
        game.placeShipsAutomatically(1);
        List<Ship> shipsToRemove = new ArrayList<>();
        shipsToRemove.add(new AircraftCarrier(new Point(1, 9), true));
        client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, shipsToRemove, true));

        // Assert
        assertEquals(SquareState.WATER, application.getPlayerSquareState(1, 9));
    }
    @Test()
    public void should_Set_ErrorMessage_When_Success_Is_False() {
        // Arrange & Act
        game.placeShipsAutomatically(1);
        client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, null, null, false));

        // Assert
        assertEquals("Failed to automatically place all ships!", application.getErrorMessage());
    }

}
