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

    private ISeaBattleGame game;
    private MockSeaBattleApplication application;
    private MockClient client;

    @BeforeEach
    public void setUp() {
        // Create the mock socket client
        client = new MockClient();

        // Create mock Sea Battle GUI for player
        application = new MockSeaBattleApplication();

        // Create the game
        game = new SeaBattleGame(application, client);
    }

    @Test()
    public void should_Set_PlayerName_To_Henk_On_Application_When_Name_Is_Karel_And_Password_Is_Karel32_And_MultiPlayer_Is_False() {
        // Arrange
        String name = "Henk";
        String password = "Karel32";
        final boolean multiPlayerMode = false;

        // Act
        game.registerPlayer(name, password, multiPlayerMode);
        client.setMockUpResponse(new RegisterResponse(1, true, -1, "CPU"));

        // Assert
        assertEquals("Henk", application.getPlayerName());
    }

    @Test()
    public void should_Throw_When_Name_Is_Null_And_Password_Is_Karel32_And_MultiPlayerMode_Is_False() {
        // Arrange
        String name = null;
        String password = "Karel32";
        final boolean multiPlayerMode = false;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, multiPlayerMode));
    }
    @Test()
    public void should_Throw_When_Name_Is_Blank_And_Password_Is_Karel32_And_MultiPlayerMode_Is_False() {
        // Arrange
        String name = "    ";
        String password = "Karel32";
        final boolean multiPlayerMode = false;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, multiPlayerMode));
    }
    @Test()
    public void should_Throw_When_Name_Is_Mario_And_Password_Is_Blank_And_MultiPlayerMode_Is_False() {
        // Arrange
        String name = "Mario";
        String password = "    ";
        final boolean multiPlayerMode = false;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, multiPlayerMode));
    }
    @Test()
    public void should_Throw_When_Name_Is_Mario_And_Password_Is_Null_And_MultiPlayerMode_Is_False() {
        // Arrange
        String name = "Mario";
        String password = null;
        final boolean multiPlayerMode = false;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, multiPlayerMode));
    }
}
