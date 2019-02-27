package seabattlegametests;

import org.junit.jupiter.api.Test;
import seabattleunittests.SeaBattleGameTest;

public class RegisterPlayerTests extends SeaBattleGameTest {
    /**
     * Example test for method registerPlayerName().
     * Test whether an IllegalArgumentException is thrown when parameter
     * name is null.
     * @author Nico Kuijpers
     */
    @Test() // expected=IllegalArgumentException.class
    public void testRegisterPlayerNameNull() {

        // Register player with parameter name null in single-player mode
        String name = null;
        String password = "password";
        boolean singlePlayerMode = true;
        game.registerPlayer(name, password, applicationPlayer, singlePlayerMode);
    }
}
