package seabattlegametests;

import org.junit.jupiter.api.Test;
import seabattlegui.SquareState;
import seabattleunittests.SeaBattleGameTest;

import static org.junit.Assert.assertEquals;

public class PlaceShipsAutomaticallyTests extends SeaBattleGameTest {
    /**
     * Example test for method placeShipsAutomatically().
     * Test whether the correct number of squares contain a ship in the
     * ocean area of the player's application.
     * @Author Nico Kuijpers
     */
    @Test
    public void testPlaceShipsAutomatically() {

        // Register player in single-player mode
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        // Place ships automatically
        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShipsAutomatically(playerNr);

        // Count number of squares where ships are placed in player's application
        int expectedResult = 5 + 4 + 3 + 3 + 2;
        int actualResult = applicationPlayer.numberSquaresPlayerWithSquareState(SquareState.SHIP);
        assertEquals("Wrong number of squares where ships are placed",expectedResult,actualResult);
    }
}
