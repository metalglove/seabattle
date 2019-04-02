/*
 * Sea Battle Start project.
 */
package seabattleunittests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;

/**
 * Unit tests for Sea Battle game.
 * @author Nico Kuijpers
 */
public abstract class SeaBattleGameTests {
    
    protected ISeaBattleGame game;
    protected MockSeaBattleApplication applicationPlayer;
    protected MockSeaBattleApplication applicationOpponent;
    
    public SeaBattleGameTests() {
    }

    @BeforeEach
    public void setUp() {
        // Create the Sea Battle game
        MockClient mockClient = new MockClient();
        game = new SeaBattleGame(applicationPlayer, mockClient);

        // Create mock Sea Battle GUI for player
        applicationPlayer = new MockSeaBattleApplication();

        // Create mock Sea Battle GUI for opponent
        applicationOpponent = new MockSeaBattleApplication();

        game.registerPlayer("player1", "sdsd", true);
    }
    
    @AfterEach
    public void tearDown() {
    }
}

