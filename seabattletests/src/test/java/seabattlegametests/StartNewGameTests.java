package seabattlegametests;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import domain.ships.*;
import messaging.messages.responses.*;
import mocks.MockClient;
import mocks.MockSeaBattleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
public class StartNewGameTests {
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

        game.registerPlayer("Mario", "password",false);
        client.setMockUpResponse(new RegisterResponse(1, true, -1, "CPU", duplicate));
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
    public void should_Create_New_Game_When_Called_SinglePlayer() {
        // Arrange
        game.fireShot(1, 1, 1);
        MineSweeper mineSweeper = new MineSweeper(new Point(1, 1), true);
        mineSweeper.removePoint(new Point(2,1));
        mineSweeper.removePoint(new Point(1,1));
        client.setMockUpResponse(new FireShotResponse(1, ShotType.ALLSUNK, new Point(1, 1), mineSweeper, true));

        // Act
        // game is actively going on
        assertTrue(application.isGameStarted());

        game.startNewGame(1, false);
        client.setMockUpResponse(new StartNewGameResponse(1, true, "CPU", -1));

        // Assert
        // game has been wiped players need to ready up again for the game to start should be false
        assertFalse(application.isGameStarted());
    }

    @Test
    public void should_Create_New_Game_When_Called_MultiPlayer() {
        // Arrange
        game.fireShot(1, 1, 1);
        MineSweeper mineSweeper = new MineSweeper(new Point(1, 1), true);
        mineSweeper.removePoint(new Point(2,1));
        mineSweeper.removePoint(new Point(1,1));
        client.setMockUpResponse(new FireShotResponse(1, ShotType.ALLSUNK, new Point(1, 1), mineSweeper, true));

        // Act
        // game is actively going on
        assertTrue(application.isGameStarted());

        game.startNewGame(1, true);
        client.setMockUpResponse(new StartNewGameResponse(1, true, null, null));

        // Assert
        // game has been wiped players need to ready up again for the game to start should be false
        assertFalse(application.isGameStarted());
    }

    @Test
    public void should_Set_ErrorMessage_When_Success_Is_False() {
        // Arrange
        game.fireShot(1, 1, 1);
        MineSweeper mineSweeper = new MineSweeper(new Point(1, 1), true);
        mineSweeper.removePoint(new Point(2,1));
        mineSweeper.removePoint(new Point(1,1));
        client.setMockUpResponse(new FireShotResponse(1, ShotType.ALLSUNK, new Point(1, 1), mineSweeper, true));

        // Act
        game.startNewGame(1, false);
        client.setMockUpResponse(new StartNewGameResponse(null, false, null, null));

        // Assert
        assertEquals("Starting a new game failed!", application.getErrorMessage());
    }
    @Test
    public void should_Not_Create_New_Game_When_Called_When_Game_Is_Not_Over_Yet() {
        // Arrange & Act
        game.startNewGame(1, false);
        // Mock response not even needed it will check before hand..

        // Assert
        assertEquals("Game has not ended yet!",  application.getErrorMessage());
    }

}
