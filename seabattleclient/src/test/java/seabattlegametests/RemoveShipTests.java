package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattlegui.ShipType;
import seabattlegui.SquareState;
import seabattleunittests.SeaBattleGameTests;

/**
 * Remove the ship that is placed at the square with coordinates (posX, posY).
 * The state of the ocean area in the player's application will be kept
 * up-to-date by method calls of showSquarePlayer().
 * param playerNr identification of player for which ship will be removed
 * param posX     x-coordinate of square where ship was placed
 * param posY     y-coordinate of square where ship was placed
 */
public class RemoveShipTests extends SeaBattleGameTests {
    // TODO: ask Nico about what happens when a coordinate is given that is out of range or is at a location that does not have a ship on it.

    @Test
    public void should_Remove_Ship_On_1X_1Y_When_Previously_Placed_On_1X_1Y() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);

        // Act
        game.removeShip(1,1,1);

        // Assert
        SquareState playerSquareState = applicationPlayer.getPlayerSquareState(1,1);
        Assertions.assertEquals(SquareState.WATER, playerSquareState);
    }

    @Test
    public void should_Remove_AIRCRAFTCARRIER_On_5X_1Y_When_Previously_Placed_On_1X_1Y() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);

        // Act
        game.removeShip(1,5,1);

        // Assert
        SquareState playerSquareState = applicationPlayer.getPlayerSquareState(1,1);
        Assertions.assertEquals(SquareState.WATER, playerSquareState);
    }
}
