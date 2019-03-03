package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattlegui.ShipType;
import seabattlegui.SquareState;
import seabattleunittests.SeaBattleGameTests;

/**
 * Remove all ships that are placed. The state of the ocean area in the
 * player's application will be kept up-to-date by method calls of
 * showSquarePlayer().
 * param playerNr  identification of player for which ships will be removed
 */
public class RemoveAllShipsTests extends SeaBattleGameTests {
    @Test
    public void Should_Remove_All_Ships_When_Ships_Have_Previously_Been_Placed() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        // Act
        game.removeAllShips(1);

        // Assert
        final SquareState playerSquareStateAfterAIRCRAFTCARRIER = applicationPlayer.getPlayerSquareState(1, 1);
        final SquareState playerSquareStateAfterBATTLESHIP = applicationPlayer.getPlayerSquareState(1, 2);
        final SquareState playerSquareStateAfterCRUISER = applicationPlayer.getPlayerSquareState(1, 3);
        final SquareState playerSquareStateAfterSUBMARINE = applicationPlayer.getPlayerSquareState(1, 4);
        final SquareState playerSquareStateAfterMINESWEEPER = applicationPlayer.getPlayerSquareState(1, 5);
        Assertions.assertEquals(SquareState.WATER, playerSquareStateAfterAIRCRAFTCARRIER);
        Assertions.assertEquals(SquareState.WATER, playerSquareStateAfterBATTLESHIP);
        Assertions.assertEquals(SquareState.WATER, playerSquareStateAfterCRUISER);
        Assertions.assertEquals(SquareState.WATER, playerSquareStateAfterSUBMARINE);
        Assertions.assertEquals(SquareState.WATER, playerSquareStateAfterMINESWEEPER);
    }
}
