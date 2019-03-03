package seabattlegametests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seabattleunittests.SeaBattleGameTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


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
public class RegisterPlayerTests extends SeaBattleGameTests {

    // TODO: register 2 same players
    // TODO: register 2 players without exceeding for multiplayer
    // TODO: de
    @Test()
    public void should_Not_Throw_When_Name_Is_Mario_And_Password_Is_Ape123_And_ApplicationPlayer_Is_Not_Null_And_SinglePlayerMode_Is_True() {
        // Arrange
        String name = "Mario";
        String password = "Ape123";
        final boolean singlePlayerMode = true;

        // Act & Assert
        assertDoesNotThrow(() -> game.registerPlayer(name, password, singlePlayerMode));
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
    /*
    @Test()
    public void should_Throw_When_Name_Is_Mario_And_Password_Is_Ape123_And_ApplicationPlayer_Is_Null_And_SinglePlayerMode_Is_True() {
        // Arrange
        String name = "Mario";
        String password = "Ape123";
        applicationPlayer = null;
        final boolean singlePlayerMode = true;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class ,() -> game.registerPlayer(name, password, applicationPlayer, singlePlayerMode));
    } */
}
