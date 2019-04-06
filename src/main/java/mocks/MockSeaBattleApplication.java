/*
 * Sea Battle Start project.
 */
package mocks;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import seabattlegui.ISeaBattleGUI;
import seabattlegui.SquareState;

/**
 * Mock Sea Battle application to support unit testing of the Sea Battle game.
 * @author Nico Kuijpers
 */
@SuppressWarnings("ALL")
public class MockSeaBattleApplication implements ISeaBattleGUI {
    
    private final int XSIZE = 10;
    private final int YSIZE = 10;
    
    private int playerNr = -1;
    private String playerName = null;
    private String opponentName = null;
    private String errorMessage = null;
    
    private boolean wrongPlayerNumberReceived = false;
    private boolean gameStarted = false;
    
    private ShotType lastShotPlayer = null;
    private ShotType lastShotOpponent = null;
    
    private SquareState[][] playerGrid = new SquareState[XSIZE][YSIZE];
    private SquareState[][] opponentGrid = new SquareState[XSIZE][YSIZE];

    @Override
    public void setPlayerNumber(int playerNr, String name) {
        // Set player number and player name
        this.playerNr = playerNr;
        this.playerName = name;
        
        // Reset state of game
        this.wrongPlayerNumberReceived = false;
        this.gameStarted = false;
        this.lastShotPlayer = null;
        this.lastShotOpponent = null;
        this.playerGrid = new SquareState[XSIZE][YSIZE];
        this.opponentGrid = new SquareState[XSIZE][YSIZE];
        for (int i = 0; i < XSIZE; i++) {
            for (int j = 0; j < YSIZE; j++) {
                playerGrid[i][j] = SquareState.WATER;
                opponentGrid[i][j] = SquareState.WATER;
            }
        }
    }

    @Override
    public void setOpponentName(int playerNr, String name) {
        this.opponentName = name;
    }
    
    @Override
    public void notifyStartGame(int playerNr) {
        this.gameStarted = true;
    }

    @Override
    public void playerFiresShot(int playerNr, ShotType shotType, Point point) {
        this.lastShotPlayer = shotType;
    }

    @Override
    public void opponentFiresShot(int playerNr, ShotType shotType, Point point) {
        this.lastShotOpponent = shotType;
    }

    @Override
    public void showSquarePlayer(int playerNr, int posX, int posY, SquareState squareState) {
        this.playerGrid[posX][posY] = squareState;
    }

    @Override
    public void showSquareOpponent(int playerNr, int posX, int posY, SquareState squareState) {
        this.opponentGrid[posX][posY] = squareState;
    }

    @Override
    public void showErrorMessage(int playerNr, String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void placeShip(int playerNr, Ship ship) {
        for (Point point : ship.getPoints()) {
            playerGrid[point.getX() - 1][point.getY() - 1] = SquareState.SHIP;
        }
    }

    @Override
    public void removeShip(int playerNumber, Ship shipToRemove) {
        for (Point point : shipToRemove.getPoints()) {
            playerGrid[point.getX() - 1][point.getY() - 1] = SquareState.WATER;
        }
    }

    @Override
    public void resetGUI() {
        // TODO: fix resetGUI in MOCK
    }

    /**
     * Get player number.
     * @return player number
     */
    public int getPlayerNumber() {
        return playerNr;
    }
    
    /**
     * Get player name.
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Get opponent name.
     * @return opponent name
     */
    public String getOpponentName() {
         return opponentName;
    }
    
    /**
     * Get the last error message.
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Check whether wrong player number was received.
     * @return true when wrong player number was received,
     * otherwise false
     */
    public boolean isWrongPlayerNumberReceived() {
        return wrongPlayerNumberReceived;
    }
    
    /**
     * Check whether game was started.
     * @return true when game was started, otherwise false
     */
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    /**
     * Get result of last shot fired by player.
     * @return result of shot fired by player
     */
    public ShotType getLastShotPlayer() {
        return lastShotPlayer;
    }

    /**
     * Get result of last shot fired by opponent.
     * @return result of last shot fired by opponent
     */
    public ShotType getLastShotOpponent() {
        return lastShotOpponent;
    }

    /**
     * Get state of square at position (posX,posY) in ocean area.
     * @param posX x-position
     * @param posY y-position
     * @return state of square (posX,posY) in ocean area
     */
    public SquareState getPlayerSquareState(int posX, int posY) {
        if (1 <= posX && posX < XSIZE + 1 && 1 <= posY && posY < YSIZE + 1) {
            return playerGrid[posX - 1][posY - 1];
        }
        System.err.println("MockSeaBattleApplication: Wrong coordinates "
                + "(" + posX + "," + posY + ") method call getPlayerSquareState()");
        return null;
    }

    /**
     * Get state of square at position (posX,posY) in target area.
     * @param posX x-position
     * @param posY y-position
     * @return state of square (posX,posY) in target area
     */
    public SquareState getOpponentSquareState(int posX, int posY) {
        if (1 <= posX && posX < XSIZE + 1 && 1 <= posY && posY < YSIZE - 1) {
            return opponentGrid[posX - 1][posY - 1];
        }
        System.err.println("MockSeaBattleApplication: Wrong coordinates "
                + "(" + posX + "," + posY + ") method call getOpponentSquareState()");
        return null;
    }
    
    /**
     * Count number of squares with given square state in ocean area.
     * @param squareState square state
     * @return number of squares with given square state in ocean area.
     */
    public int numberSquaresPlayerWithSquareState(SquareState squareState) {
        int count = 0;
        for (int i = 0; i < XSIZE; i++) {
            for (int j = 0; j < YSIZE; j++) {
                if (playerGrid[i][j] == squareState) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Count number of squares with given square state in target area.
     * @param squareState square state
     * @return number of squares with given square state in target area.
     */
    public int numberSquaresOpponentWithSquareState(SquareState squareState) {
        int count = 0;
        for (int i = 0; i < XSIZE; i++) {
            for (int j = 0; j < YSIZE; j++) {
                if (opponentGrid[i][j] == squareState) {
                    count++;
                }
            }
        }
        return count;
    }
}
