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

    // TODO: check if previous ships are removed? (But how?..., placements are random so they could be on the same spot.)

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
}
