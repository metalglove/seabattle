package seabattlegametests;

import domain.Point;
import domain.ShipType;
import domain.ships.*;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.messages.responses.PlaceShipResponse;
import messaging.messages.responses.RegisterResponse;
import messaging.messages.responses.RemoveShipResponse;
import mocks.MockClient;
import mocks.MockSeaBattleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattlegui.SquareState;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Remove the ship that is placed at the square with coordinates (posX, posY).
 * The state of the ocean area in the player's application will be kept
 * up-to-date by method calls of showSquarePlayer().
 * param playerNr identification of player for which ship will be removed
 * param posX     x-coordinate of square where ship was placed
 * param posY     y-coordinate of square where ship was placed
 */
public class RemoveShipTests {
    private ISeaBattleGame game;
    private MockSeaBattleApplication application;
    private MockClient client;

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
        client.setMockUpResponse(new RegisterResponse(1, true, -1, "CPU"));
    }

    @Test
    public void should_Remove_Ship_On_1X_1Y_When_Previously_Placed_On_1X_1Y() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new AircraftCarrier(new Point(1,1), true), true, null,false));

        // Act
        game.removeShip(1,1,1);
        client.setMockUpResponse(new RemoveShipResponse(1, new AircraftCarrier(new Point(1,1), true), true));

        // Assert
        assertEquals(SquareState.WATER, application.getPlayerSquareState(1, 1));
    }

    @Test
    public void should_Remove_AIRCRAFTCARRIER_On_5X_1Y_When_Previously_Placed_On_1X_1Y() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new AircraftCarrier(new Point(1,1), true), true, null,false));

        // Act
        game.removeShip(1,5,1);
        client.setMockUpResponse(new RemoveShipResponse(1, new AircraftCarrier(new Point(1,1), true), true));

        // Assert
        assertEquals(SquareState.WATER, application.getPlayerSquareState(1, 1));
    }

    @Test
    public void should_Set_ErrorMessage_When_Player_Is_Already_Ready() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new AircraftCarrier(new Point(1, 1), true), true,null, false));
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new BattleShip(new Point(1, 2), true), true,null, false));
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new Cruiser(new Point(1, 3), true), true,null, false));
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new Submarine(new Point(1, 4), true), true,null, false));
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new MineSweeper(new Point(1, 5), true), true,null, true));
        game.notifyWhenReady(1);
        client.setMockUpResponse(new NotifyWhenReadyResponse(1, true, true));

        // Act
        game.removeShip(1,1,1);

        // Assert
        assertEquals("You are not allowed to change your ships after readying up.", application.getErrorMessage());
    }

    @Test
    public void should_Set_ErrorMessage_When_Success_Is_False() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new AircraftCarrier(new Point(1,1), true), true, null,false));

        // Act
        game.removeShip(1,2,1);
        client.setMockUpResponse(new RemoveShipResponse(null, null, false));

        // Assert
        assertEquals("Failed to remove ship from proposed point.", application.getErrorMessage());
    }
}
