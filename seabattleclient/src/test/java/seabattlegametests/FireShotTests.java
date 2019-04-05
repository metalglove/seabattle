package seabattlegametests;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import domain.ships.*;
import messaging.messages.responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattleunittests.MockClient;
import seabattleunittests.MockSeaBattleApplication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class FireShotTests {
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
        AircraftCarrier aircraftCarrier = new AircraftCarrier(new Point(1, 1), true);
        BattleShip battleShip = new BattleShip(new Point(1, 2), true);
        Cruiser cruiser = new Cruiser(new Point(1, 3), true);
        MineSweeper mineSweeper = new MineSweeper(new Point(1, 4), true);
        Submarine submarine = new Submarine(new Point(1, 5), true);
        List<Ship> ships = new ArrayList<>();
        ships.add(aircraftCarrier);
        ships.add(battleShip);
        ships.add(cruiser);
        ships.add(mineSweeper);
        ships.add(submarine);
        game.placeShipsAutomatically(1);
        client.setMockUpResponse(new PlaceShipsAutomaticallyResponse(1, ships, new ArrayList<>(), true));
        game.notifyWhenReady(1);
        client.setMockUpResponse(new NotifyWhenReadyResponse(1, true, true));
    }

    @Test
    public void should_Hit_Opponents_Ship_On_X1_Y1_When_Shot_On_X1_Y1() {
        // Arrange & Act
        game.fireShot(1, 1, 1);
        Point x1y1 = new Point(1,1);
        BattleShip battleShip = new BattleShip(x1y1, true);
        battleShip.removePoint(x1y1);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, x1y1, battleShip, true));

        // Assert
        assertEquals(ShotType.HIT, application.getLastShotPlayer());
    }

    @Test
    public void should_Sink_Opponents_Ship_On_X1_Y1_When_Shot_On_All_Ship_Coordinates() {
        // Arrange
        Point xy = new Point(1,1);
        BattleShip battleShip = new BattleShip(xy, true);
        Point xy2 = new Point(1,1);
        BattleShip battleShip2 = new BattleShip(xy2, true);

        // Act
        // Player shot: 1
        game.fireShot(1, 1, 1);
        battleShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, battleShip, true));

        // Opponent shot: 1
        game.fireShot(-1, 1, 1);
        battleShip2.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, battleShip2, true));

        // Player shot: 2
        game.fireShot(1, 2, 1);
        xy = new Point(2, 1);
        battleShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, battleShip, true));

        // Opponent shot: 2
        game.fireShot(-1, 2, 1);
        xy2 = new Point(2, 1);
        battleShip2.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, battleShip2, true));

        // Player shot: 3
        game.fireShot(1, 3, 1);
        xy = new Point(3, 1);
        battleShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, battleShip, true));

        // Opponent shot: 3
        game.fireShot(-1, 3, 1);
        xy2 = new Point(3, 1);
        battleShip2.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, battleShip2, true));

        // Player shot: 4
        game.fireShot(1, 4, 1);
        xy = new Point(4, 1);
        battleShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.SUNK, xy, battleShip, true));

        // Assert
        assertEquals(ShotType.SUNK, application.getLastShotPlayer());
    }
    @Test
    public void should_Return_AllSunk_When_All_Ship_Coordinates_Sunk() {
        // Arrange & Act
        Point xy = new Point(1,1);
        Ship playerShip = new BattleShip(xy, true);
        Point xy2 = new Point(1,1);
        Ship opponentShip = new BattleShip(xy2, true);

        // Player shot: 1
        game.fireShot(1, 1, 1);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 1
        game.fireShot(-1, 1, 1);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 2
        game.fireShot(1, 2, 1);
        xy = new Point(2, 1);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 2
        game.fireShot(-1, 2, 1);
        xy2 = new Point(2, 1);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 3
        game.fireShot(1, 3, 1);
        xy = new Point(3, 1);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 3
        game.fireShot(-1, 3, 1);
        xy2 = new Point(3, 1);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 4
        game.fireShot(1, 4, 1);
        xy = new Point(4, 1);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.SUNK, xy, playerShip, true));

        // Opponent shot: 4
        game.fireShot(-1, 4, 1);
        xy2 = new Point(4, 1);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.SUNK, opponentShip, true));

        playerShip = new AircraftCarrier(xy, true);
        opponentShip = new AircraftCarrier(xy2, true);

        // Player shot: 1
        game.fireShot(1, 1, 2);
        xy = new Point(1,2);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 1
        game.fireShot(-1, 1, 2);
        xy2 = new Point(1,2);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 2
        game.fireShot(1, 2, 2);
        xy = new Point(2,2);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 2
        game.fireShot(-1, 2, 2);
        xy2 = new Point(2,2);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 3
        game.fireShot(1, 3, 2);
        xy = new Point(3,2);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 3
        game.fireShot(-1, 3, 2);
        xy2 = new Point(3,2);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 4
        game.fireShot(1, 4, 2);
        xy = new Point(4,2);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 4
        game.fireShot(-1, 4, 2);
        xy2 = new Point(4,2);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 5
        game.fireShot(1, 5, 2);
        xy = new Point(5,2);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.SUNK, xy, playerShip, true));

        // Opponent shot: 5
        game.fireShot(-1, 5, 2);
        xy2 = new Point(5,2);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.SUNK, opponentShip, true));

        playerShip = new Submarine(xy, true);
        opponentShip = new Submarine(xy2, true);

        // Player shot: 1
        game.fireShot(1, 1, 3);
        xy = new Point(1,3);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 1
        game.fireShot(-1, 1, 3);
        xy2 = new Point(1,3);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 2
        game.fireShot(1, 2, 3);
        xy = new Point(2,3);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 2
        game.fireShot(-1, 2, 3);
        xy2 = new Point(2,3);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 3
        game.fireShot(1, 3, 3);
        xy = new Point(3,3);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.SUNK, xy, playerShip, true));

        // Opponent shot: 3
        game.fireShot(-1, 3, 3);
        xy2 = new Point(3,3);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.SUNK, opponentShip, true));

        playerShip = new MineSweeper(xy, true);
        opponentShip = new MineSweeper(xy2, true);

        // Player shot: 1
        game.fireShot(1, 1, 4);
        xy = new Point(1,4);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 1
        game.fireShot(-1, 1, 4);
        xy2 = new Point(1,4);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 2
        game.fireShot(1, 2, 4);
        xy = new Point(2,4);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.SUNK, xy, playerShip, true));

        // Opponent shot: 2
        game.fireShot(-1, 2, 4);
        xy2 = new Point(2,4);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.SUNK, opponentShip, true));

        playerShip = new Cruiser(xy, true);
        opponentShip = new Cruiser(xy2, true);

        // Player shot: 1
        game.fireShot(1, 1, 5);
        xy = new Point(1,5);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 1
        game.fireShot(-1, 1, 5);
        xy2 = new Point(1,5);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 2
        game.fireShot(1, 2, 5);
        xy = new Point(2,5);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.HIT, xy, playerShip, true));

        // Opponent shot: 2
        game.fireShot(-1, 2, 5);
        xy2 = new Point(2,5);
        opponentShip.removePoint(xy2);
        client.setMockUpResponse(new OpponentFireShotResponse(-1, xy2, ShotType.HIT, opponentShip, true));

        // Player shot: 3
        game.fireShot(1, 3, 5);
        xy = new Point(3,5);
        playerShip.removePoint(xy);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.ALLSUNK, xy, playerShip, true));

        // Assert
        assertEquals(ShotType.ALLSUNK, application.getLastShotPlayer());
    }

    @Test
    public void should_Miss_Opponents_Ship_When_Shot_On_X6_Y6() {
        // Arrange & Act
        game.fireShot(1, 6, 6);
        client.setMockUpResponse(new FireShotResponse(1, ShotType.MISSED, new Point(6, 6), null, true));

        // Assert
        assertEquals(ShotType.MISSED, application.getLastShotPlayer());
    }

    @Test
    public void should_Be_Out_Of_Range_When_Shot_On_X11_Y11() {
        // Arrange & Act
        game.fireShot(1,11,11);
        // Mock response not even needed it will check before hand..

        // Assert
        assertEquals("Position is out of range!", application.getErrorMessage());
    }
}
