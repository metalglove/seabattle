package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattlegui.ShipType;
import seabattlegui.ShotType;
import seabattlegui.SquareState;
import static org.junit.jupiter.api.Assertions.assertThrows;
import seabattleunittests.SeaBattleGameTests;

/**
 * Fire a shot at the opponent's square with given coordinates.
 * Firing a shot gives one of the following results:
 * ShotType.MISSED - No ship was hit
 * ShotType.HIT - A ship has been hit
 * ShotType.SUNK - A ship has been sunk (all squares of the ship have been hit)
 * ShotType.ALLSUNK - All ships have been sunk (all other ships were already sunk)
 * The result of the shot will sent to the player's application by a method call
 * of playerFiresShot() and to the opponent's application by a method call
 * of opponentFiresShot(). The target area in the player's application
 * will be kept up-to-date by method calls of showSquareOpponent() and the ocean
 * area in the opponent's application by method calls of showSquarePlayer().
 * In single-player mode the opponent will fire a shot using an AI strategy.
 * The result of the opponent's shot will be sent to the player's application
 * by a method call of opponentFiresShot() and the ocean area of the player's
 * application will by kept up-to-date by method calls of showSquarePlayer().
 * param playerNr identification of player who fires.
 * param posX     x-coordinate of square
 * param posY     y-coordinate of square
 */
public class FireShotTests extends SeaBattleGameTests {

    @Test
    public void should_Hit_Opponents_Ship_On_X1_Y1_When_Shot_On_X1_Y1() {
        // Arrange
        game.startNewGame(1);
        game.registerPlayer("player1", "sds", applicationPlayer, false);
        game.registerPlayer("player2", "sds", applicationOpponent, false);
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        game.placeShip(2, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(2, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(2, ShipType.CRUISER, 1,3,true);
        game.placeShip(2, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(2, ShipType.MINESWEEPER, 1,5,true);
        game.notifyWhenReady(1);
        game.notifyWhenReady(2);
        applicationPlayer.notifyStartGame(1);
        applicationPlayer.notifyStartGame(2);

        // Act
        game.fireShot(1, 1, 1);

        // Assert
        final ShotType lastShotOpponent = applicationOpponent.getLastShotOpponent();
        Assertions.assertEquals(ShotType.HIT, lastShotOpponent);

    }
    @Test
    public void should_Sink_Opponents_Ship_On_X1_Y1_When_Shot_On_All_Ship_Coordinates() {
        // Arrange
        game.startNewGame(1);
        game.registerPlayer("player1", "sds", applicationPlayer, false);
        game.registerPlayer("player2", "sds", applicationOpponent, false);
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        game.placeShip(2, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(2, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(2, ShipType.CRUISER, 1,3,true);
        game.placeShip(2, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(2, ShipType.MINESWEEPER, 1,5,true);
        game.notifyWhenReady(1);
        game.notifyWhenReady(2);
        applicationPlayer.notifyStartGame(1);
        applicationPlayer.notifyStartGame(2);

        // Act
        game.fireShot(1, 1, 1);
        game.fireShot(1, 2, 1);
        game.fireShot(1, 3, 1);
        game.fireShot(1, 4, 1);
        game.fireShot(1, 5, 1);

        // Assert
        final ShotType lastShotOpponent = applicationOpponent.getLastShotOpponent();

        Assertions.assertEquals(ShotType.SUNK, lastShotOpponent);
    }
    @Test
    public void should_Return_Allsunk_When_All_Ship_Coordinates_Sunk() {
        // Arrange
        game.startNewGame(1);
        game.registerPlayer("player1", "sds", applicationPlayer, false);
        game.registerPlayer("player2", "sds", applicationOpponent, false);
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        game.placeShip(2, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(2, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(2, ShipType.CRUISER, 1,3,true);
        game.placeShip(2, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(2, ShipType.MINESWEEPER, 1,5,true);
        game.notifyWhenReady(1);
        game.notifyWhenReady(2);
        applicationPlayer.notifyStartGame(1);
        applicationPlayer.notifyStartGame(2);

        // Act
        // AIRCRAFTCARRIER
        game.fireShot(1, 1, 1);
        game.fireShot(1, 2, 1);
        game.fireShot(1, 3, 1);
        game.fireShot(1, 4, 1);
        game.fireShot(1, 5, 1);
        // BATTLESHIP
        game.fireShot(1, 1, 2);
        game.fireShot(1, 2, 2);
        game.fireShot(1, 3, 2);
        game.fireShot(1, 4, 2);
        //CRUISER
        game.fireShot(1, 1, 3);
        game.fireShot(1, 2, 3);
        game.fireShot(1, 3, 3);
        //SUBMARINE
        game.fireShot(1, 1, 4);
        game.fireShot(1, 2, 4);
        game.fireShot(1, 3, 4);
        //MINESWEEPER
        game.fireShot(1, 1, 5);
        game.fireShot(1, 2, 5);

        // Assert
        final ShotType lastShotOpponent = applicationOpponent.getLastShotOpponent();

        Assertions.assertEquals(ShotType.ALLSUNK, lastShotOpponent);
    }
    @Test
    public void should_Miss_Opponents_Ship_When_Shot_On_X6_Y6() {
        // Arrange
        game.startNewGame(1);
        game.registerPlayer("player1", "sds", applicationPlayer, false);
        game.registerPlayer("player2", "sds", applicationOpponent, false);
        game.placeShip(1, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(1, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(1, ShipType.CRUISER, 1,3,true);
        game.placeShip(1, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(1, ShipType.MINESWEEPER, 1,5,true);

        game.placeShip(2, ShipType.AIRCRAFTCARRIER, 1,1,true);
        game.placeShip(2, ShipType.BATTLESHIP, 1,2,true);
        game.placeShip(2, ShipType.CRUISER, 1,3,true);
        game.placeShip(2, ShipType.SUBMARINE, 1,4,true);
        game.placeShip(2, ShipType.MINESWEEPER, 1,5,true);
        game.notifyWhenReady(1);
        game.notifyWhenReady(2);
        applicationPlayer.notifyStartGame(1);
        applicationPlayer.notifyStartGame(2);

        // Act
        game.fireShot(1, 6, 6);

        // Assert
        final ShotType lastShotOpponent = applicationOpponent.getLastShotOpponent();
        Assertions.assertEquals(ShotType.MISSED, lastShotOpponent);
    }
    @Test
    public void should_Be_Out_Of_Range_When_Shot_On_X11_Y11() {
        // Arrange
        game.startNewGame(1);
        game.registerPlayer("player1", "sds", applicationPlayer, false);
        game.registerPlayer("player2", "sds", applicationOpponent, false);

        game.notifyWhenReady(1);
        game.notifyWhenReady(2);
        applicationPlayer.notifyStartGame(1);
        applicationPlayer.notifyStartGame(2);

        // Act
        Exception thrown = assertThrows(Exception.class,
                        () -> game.fireShot(1,11,11),
                        "Tried to fireShot(), but position was out of range.");

        // Assert
        Assertions.assertTrue(thrown.getMessage().contains("Tried to fireShot(), but position was out of range."));
    }
}
