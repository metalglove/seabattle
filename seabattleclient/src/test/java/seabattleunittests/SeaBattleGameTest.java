/*
 * Sea Battle Start project.
 */
package seabattleunittests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattlegui.SquareState;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for Sea Battle game.
 * @author Nico Kuijpers
 */
public abstract class SeaBattleGameTest {
    
    protected ISeaBattleGame game;
    protected MockSeaBattleApplication applicationPlayer;
    protected MockSeaBattleApplication applicationOpponent;
    
    public SeaBattleGameTest() {
    }

    @BeforeEach
    public void setUp() {
        
        // Create the Sea Battle game
        game = new SeaBattleGame();
        
        // Create mock Sea Battle GUI for player
        applicationPlayer = new MockSeaBattleApplication();
        
        // Create mock Sea Battle GUI for opponent
        applicationOpponent = new MockSeaBattleApplication();
    }
    
    @AfterEach
    public void tearDown() {
    }
}

