package seabattlegametests;

import domain.Point;
import domain.ShipType;
import domain.ships.*;
import messaging.messages.responses.PlaceShipResponse;
import messaging.messages.responses.RegisterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattlegui.SquareState;
import seabattleunittests.MockClient;
import seabattleunittests.MockSeaBattleApplication;
import seabattleunittests.SeaBattleGameTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Place ship of given type. A ship of given type will be placed with its
 * bow at the given coordinates.
 * If horizontal = true, the stern will be placed to the right of the bow.
 * If horizontal = false, the stern will be placed below the bow.
 * The ship will be placed only if it fits entirely within the grid and
 * has no overlap with other ships. If a ship of given type has already been
 * placed, that ship will be removed. The state of the ocean area in the
 * player's application will be kept up-to-date by method calls of
 * showSquarePlayer().
 * param playerNr   identification of player for which ship will be placed
 * param shipType   type of ship to be placed
 * param bowX       x-coordinate of bow
 * param bowY       y-coordinate of bow
 * param horizontal indicate whether ship will placed horizontally or vertically
 */
public class PlaceShipTests {
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
    public void should_Not_Place_Cruiser_On_X3_Y3_Horizontally_True_When_A_BattleShip_Is_Placed_On_X2_Y2_Horizontally_Is_False() {
        // Arrange
        game.placeShip(1, ShipType.BATTLESHIP, 2,2,false);
        client.setMockUpResponse(new PlaceShipResponse(1, new BattleShip(new Point(2, 2), false), true,null, false));

        // Act
        game.placeShip(1, ShipType.CRUISER, 3,3,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new Cruiser(new Point(3, 3), true), false, null, false));

        // Assert
        Assertions.assertEquals(SquareState.WATER, application.getPlayerSquareState(4, 3));
    }

    @Test
    public void should_Contain_17_SquareStates_Of_Ship_When_All_Ships_Are_Successfully_Placed() {
        // Arrange & Act
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

        // Assert
        assertEquals(17, application.numberSquaresPlayerWithSquareState(SquareState.SHIP));
    }

     @Test
    public void should_Remove_First_Placed_BattleShip_When_Second_Is_Placed() {
         // Arrange
         game.placeShip(1, ShipType.BATTLESHIP, 2,2,false);
         client.setMockUpResponse(new PlaceShipResponse(1, new BattleShip(new Point(2, 2), false), true,null, false));

         // Act
         game.placeShip(1, ShipType.BATTLESHIP, 3,3,true);
         client.setMockUpResponse(new PlaceShipResponse(1, new BattleShip(new Point(3, 3), true), true, new BattleShip(new Point(2, 2), false), false));

        // Assert
         Assertions.assertEquals(SquareState.WATER, application.getPlayerSquareState(2, 2));
    }

    @Test
    public void should_Not_Place_BattleShip_On_9X_4Y_When_Horizontally_Is_True() {
        // Arrange & Act
        game.placeShip(1, ShipType.BATTLESHIP, 9,4,true);
        client.setMockUpResponse(new PlaceShipResponse(1, new BattleShip(new Point(9, 4), true), false,null, false));

        // Assert
        assertEquals(SquareState.WATER, application.getPlayerSquareState(9, 4));
    }

    @Test
    public void should_Not_Place_BattleShip_On_4X_7Y_When_Horizontally_Is_False() {
        // Arrange & Act
        game.placeShip(1, ShipType.BATTLESHIP, 4,7,false);
        client.setMockUpResponse(new PlaceShipResponse(1, new BattleShip(new Point(4, 7), false), false,null, false));

        // Assert
        assertEquals(SquareState.WATER, application.getPlayerSquareState(4, 7));
    }
}
