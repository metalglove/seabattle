package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattlegame.SeaBattleGame;
import domain.ShipType;
import seabattlegui.SquareState;
import seabattleunittests.MockSeaBattleApplication;
import seabattleunittests.SeaBattleGameTests;

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
public class PlaceShipTests extends SeaBattleGameTests {
    @Test
    public void should_Not_Place_Ship_On_Wrong_playerGame() {
        // Arrange
        applicationPlayer = new MockSeaBattleApplication();
        game = new SeaBattleGame(applicationPlayer);
        //applicationOpponent = new MockSeaBattleApplication();
        game.registerPlayer("player1", "sdsd", false);
        game.registerPlayer("player2", "sdsd", false);

        SquareState expectedPlayer2Value = SquareState.WATER;

        // Act
        game.placeShip(1, ShipType.CRUISER, 3,3,true);

        // Assert
        final SquareState player2SquareState = applicationOpponent.getPlayerSquareState(3, 3);
        Assertions.assertEquals(expectedPlayer2Value, player2SquareState);
    }

    @Test
    public void should_Not_Place_Cruiser_On_X3_Y3_Horizontally_True_When_A_BattleShip_Is_Placed_On_X4_Y4_Horizontally_Is_False() {
        // Arrange
        game.placeShip(1, ShipType.BATTLESHIP, 4,2,false);

        // Act
        game.placeShip(1, ShipType.CRUISER, 3,3,true);

        // Assert
        final SquareState playerSquareState = applicationPlayer.getPlayerSquareState(3, 3);

        Assertions.assertEquals(SquareState.WATER, playerSquareState);
    }

    @Test
    public void should_Contain_15_SquareStates_Of_Ship_When_All_Ships_Are_Successfully_Placed() {
        // Arrange
        int expected = 15;

        // Act
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        // Assert
        final int actualShipSquareCount = applicationPlayer.numberSquaresPlayerWithSquareState(SquareState.SHIP);
        Assertions.assertEquals(expected, actualShipSquareCount);
    }

    @Test
    public void should_Place_5_Different_Ships_Horizontally_Below_Each_Other_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBeforeAIRCRAFTCARRIER = applicationPlayer.getPlayerSquareState(1, 1);
        final SquareState playerSquareStateBeforeBATTLESHIP = applicationPlayer.getPlayerSquareState(1, 2);
        final SquareState playerSquareStateBeforeCRUISER = applicationPlayer.getPlayerSquareState(1, 3);
        final SquareState playerSquareStateBeforeSUBMARINE = applicationPlayer.getPlayerSquareState(1, 4);
        final SquareState playerSquareStateBeforeMINESWEEPER = applicationPlayer.getPlayerSquareState(1, 5);

        // Act
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        // Assert
        final SquareState playerSquareStateAfterAIRCRAFTCARRIER = applicationPlayer.getPlayerSquareState(1, 1);
        final SquareState playerSquareStateAfterBATTLESHIP = applicationPlayer.getPlayerSquareState(1, 2);
        final SquareState playerSquareStateAfterCRUISER = applicationPlayer.getPlayerSquareState(1, 3);
        final SquareState playerSquareStateAfterSUBMARINE = applicationPlayer.getPlayerSquareState(1, 4);
        final SquareState playerSquareStateAfterMINESWEEPER = applicationPlayer.getPlayerSquareState(1, 5);

        Assertions.assertNotEquals(playerSquareStateBeforeAIRCRAFTCARRIER, playerSquareStateAfterAIRCRAFTCARRIER);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfterAIRCRAFTCARRIER);
        Assertions.assertNotEquals(playerSquareStateBeforeBATTLESHIP, playerSquareStateAfterBATTLESHIP);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfterBATTLESHIP);
        Assertions.assertNotEquals(playerSquareStateBeforeCRUISER, playerSquareStateAfterCRUISER);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfterCRUISER);
        Assertions.assertNotEquals(playerSquareStateBeforeSUBMARINE, playerSquareStateAfterSUBMARINE);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfterSUBMARINE);
        Assertions.assertNotEquals(playerSquareStateBeforeMINESWEEPER, playerSquareStateAfterMINESWEEPER);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfterMINESWEEPER);
    }

    @Test
    public void should_Remove_First_Placed_BattleShip_When_Second_Is_Placed() {
        // Arrange
        game.placeShip(1, ShipType.BATTLESHIP, 3,3,true);
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(3, 3);

        // Act
        game.placeShip(1, ShipType.BATTLESHIP, 4,4,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(3, 3);
        final SquareState playerSquareStateAfterFor2ndShip = applicationPlayer.getPlayerSquareState(4, 4);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.WATER, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfterFor2ndShip);
    }

    @Test
    public void should_Place_MineSweeper_On_3X_Y3_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(3, 3);

        // Act
        game.placeShip(1, ShipType.MINESWEEPER, 3,3,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(3, 3);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Place_Cruiser_On_3X_Y3_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(3, 3);

        // Act
        game.placeShip(1, ShipType.CRUISER, 3,3,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(3, 3);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Place_Submarine_On_3X_Y3_When_Horizontally_Is_False() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(3, 3);

        // Act
        game.placeShip(1, ShipType.SUBMARINE, 3,3,false);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(3, 3);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Place_BattleShip_On_4X_4Y_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(4, 4);

        // Act
        game.placeShip(1, ShipType.BATTLESHIP, 4,4,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(4, 4);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Not_Place_BattleShip_On_9X_4Y_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(9, 4);

        // Act
        game.placeShip(1, ShipType.BATTLESHIP, 9,4,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(9, 4);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Not_Place_BattleShip_On_4X_5Y_When_Horizontally_Is_False() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(4, 5);

        // Act
        game.placeShip(1, ShipType.BATTLESHIP, 4,5,false);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(4, 5);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Place_AircraftCarrier_On_1X_1Y_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(1, 1);

        // Act
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(1, 1);
        Assertions.assertNotEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Not_Place_AircraftCarrier_On_1X_1Y_When_Horizontally_Is_False() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(1, 1);

        // Act
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,false);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(1, 1);
        Assertions.assertEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertNotEquals(SquareState.SHIP, playerSquareStateAfter);
    }

    @Test
    public void should_Not_Place_AircraftCarrier_On_6X_1Y_When_Horizontally_Is_True() {
        // Arrange
        final SquareState playerSquareStateBefore = applicationPlayer.getPlayerSquareState(6, 1);

        // Act
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 6,1,true);

        // Assert
        final SquareState playerSquareStateAfter = applicationPlayer.getPlayerSquareState(6, 1);
        Assertions.assertEquals(playerSquareStateBefore, playerSquareStateAfter);
        Assertions.assertNotEquals(SquareState.SHIP, playerSquareStateAfter);
    }
}
