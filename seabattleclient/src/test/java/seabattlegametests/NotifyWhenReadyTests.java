package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattlegui.ShipType;
import seabattleunittests.SeaBattleGameTests;

/**
 * Notify that the player is ready to play the game, i.e., all ships have
 * been placed. When not all ships have been placed, the message
 * "Not all ships have been placed!" will be sent to the player's application
 * by a method call of showErrorMessage().
 * param playerNr identification of player who is ready to play the game
 */
public class NotifyWhenReadyTests extends SeaBattleGameTests {
    @Test
    public void Should_Notify_Player_When_All_Ships_Have_Been_Placed() {
        // Arrange
        game.placeShipsAutomatically(1);

        // Act
        game.notifyWhenReady(1);

        // Assert
        String errorMessage = applicationPlayer.getErrorMessage();
        Assertions.assertNotEquals("Not all ships have been placed!", errorMessage);
    }

    @Test
    public void Should_Set_ErrorMessage_When_Not_All_Ships_Have_Been_Placed_After_Calling() {
        // Arrange
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1, 1,true);

        // Act
        game.notifyWhenReady(1);

        // Assert
        String errorMessage = applicationPlayer.getErrorMessage();
        Assertions.assertEquals("Not all ships have been placed!", errorMessage);
    }
}
