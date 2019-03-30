package seabattlegametests;

import org.junit.jupiter.api.Test;
import domain.ShipType;
import seabattleunittests.SeaBattleGameTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Start a new game. Remove all ships and unregister the player.
 * The ocean area of the player's application will be kept up-to-date by
 * method calls of showSquarePlayer() and the target area of the opponent's
 * application will be kept up-to-date by method calls of showSquareOpponent().
 * In single-player mode also the ships of the opponent will be removed and
 * the target area of the player's application will be updated by method
 * calls of showSquareOpponent().
 * param playerNr identification of player who starts a new game
 */
public class StartNewGameTests extends SeaBattleGameTests {
    // TODO: test for multi-player too
    @Test
    public void Should_Create_New_Game_When_Called() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);

        // Act
        game.startNewGame(1, true);

        // Assert
        assertDoesNotThrow(() -> game.registerPlayer("jan", "asdsd", true));
    }
}
