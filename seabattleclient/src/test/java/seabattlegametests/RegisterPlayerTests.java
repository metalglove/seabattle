package seabattlegametests;

import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.RegisterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattlegui.ISeaBattleGUI;
import seabattleunittests.MockClient;
import seabattleunittests.MockSeaBattleApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Register player with given name and password. The player number will be
 * set at the player's application by a call-back of method setPlayerNumber().
 * param name              Name of the player to be registered
 * param password          Password of the player to be registered
 * param application       Reference to application of player
 * param singlePlayerMode  Single-player (true) or multi-player (false) mode
 * throws IllegalArgumentException when:
 * name is null or the empty,
 * password is null or empty,
 * application is null,
 * number of players exceeds one in single-player mode,
 * number of players exceeds two in multi-player mode or
 * name is already registered.
 */
public class RegisterPlayerTests {

    // TODO: register 2 same players
    // TODO: register 2 players without exceeding for multiplayer
    // TODO: de
    private ISeaBattleGame game;
    private ISeaBattleGUI application;
    private ObservableClientSocket client;

    @BeforeEach
    public void setUp() {
        // Create the mock socket client
        client = new MockClient();

        // Create mock Sea Battle GUI for player
        application = new MockSeaBattleApplication();

        game = new SeaBattleGame(application, client);
    }

    @Test()
    public void should_Pass_When_Name_Is_Mario_And_Password_Is_Ape123_And_MultiPlayer_Is_False() {
        // Arrange
        String name = "Mario";
        String password = "Ape123";
        final boolean multiPlayerMode = false;

        // Act
        game.registerPlayer(name, password, multiPlayerMode);

        // Assert
        RegisterResponse message = (RegisterResponse)client.getMessages().get(0);
        assertTrue(message.getSuccess());
        assertEquals(1, (int)message.getPlayerNumber());
        assertEquals(-1, (int)message.getOpponentPlayerNumber());
        assertEquals("CPU", message.getOpponentName());
    }

    @Test()
    public void should_Throw_When_Name_Is_Null_And_Password_Is_Ape123_And_ApplicationPlayer_Is_Not_Null_And_SinglePlayerMode_Is_True() {
        // Arrange
        String name = null;
        String password = "Ape123";
        final boolean singlePlayerMode = true;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, singlePlayerMode));
    }
    @Test()
    public void should_Throw_When_Name_Is_Mario_And_Password_Is_Null_And_ApplicationPlayer_Is_Not_Null_And_SinglePlayerMode_Is_True() {
        // Arrange
        String name = "Mario";
        String password = null;
        final boolean singlePlayerMode = true;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, singlePlayerMode));
    }
}
