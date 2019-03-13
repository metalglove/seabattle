package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattlegui.SquareState;
import seabattleunittests.SeaBattleGameTests;

/**
 * Place ships automatically. Ships that are already placed will be removed.
 * All ships will be placed such that they fit entirely within the grid
 * and have no overlap with each other. The state of the ocean area in the
 * player's application will be kept up-to-date by method calls of
 * showSquarePlayer().
 * param playerNr identification of player for which ships will be placed
 */
public class PlaceShipsAutomaticallyTests extends SeaBattleGameTests {

    //
    @Test
    public void should_Place_All_Ships_Successfully() {
        // Arrange
        int playerNr = applicationPlayer.getPlayerNumber();
        int expectedResult = 5 + 4 + 3 + 3 + 2;

        // Act
        game.placeShipsAutomatically(playerNr);

        // Assert
        int actualResult = applicationPlayer.numberSquaresPlayerWithSquareState(SquareState.SHIP);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void should_Remove_All_Placed_Ships_Successfully() {
        // Arrange
        int playerNr = applicationPlayer.getPlayerNumber();
        int expectedResult = 0;

        // Act
        game.placeShipsAutomatically(playerNr);
        game.removeAllShips(1);

        // Assert
        int actualResult = applicationPlayer.numberSquaresPlayerWithSquareState(SquareState.SHIP);
        Assertions.assertEquals(expectedResult, actualResult);
    }

}
