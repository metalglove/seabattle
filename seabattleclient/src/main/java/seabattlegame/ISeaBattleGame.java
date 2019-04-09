/*
 * Sea Battle Start project.
 */
package seabattlegame;

import domain.ShipType;

/**
 * Interface provided by the Sea Battle game to the graphical user interface.
 *
 * @author Nico Kuijpers
 */
public interface ISeaBattleGame {

    /**
     * Login player with given name and password. The player number will be
     * set at the player's application by a call-back of method setPlayerNumber().
     *
     * @param name     Name of the player to be registered
     * @param password Password of the player to be registered
     * @param multiPlayer  Multi-Player (true) or Single-Player (false) mode
     * @throws IllegalArgumentException when:
     *                                  name is null or the empty,
     *                                  password is null or empty,
     *                                  application is null,
     *                                  number of players exceeds one in single-player mode,
     *                                  number of players exceeds two in multi-player mode or
     *                                  name is already registered.
     */
    void loginPlayer(String name, String password, boolean multiPlayer);

    /**
     * Place ships automatically. Ships that are already placed will be removed.
     * All ships will be placed such that they fit entirely within the grid
     * and have no overlap with each other. The state of the ocean area in the
     * player's application will be kept up-to-date by method calls of
     * showSquarePlayer().
     *
     * @param playerNr identification of player for which ships will be placed
     */
    void placeShipsAutomatically(int playerNr);

    /**
     * Place ship of given type. A ship of given type will be placed with its
     * bow at the given coordinates.
     * If horizontal = true, the stern will be placed to the right of the bow.
     * If horizontal = false, the stern will be placed below the bow.
     * The ship will be placed only if it fits entirely within the grid and
     * has no overlap with other ships. If a ship of given type has already been
     * placed, that ship will be removed. The state of the ocean area in the
     * player's application will be kept up-to-date by method calls of
     * showSquarePlayer().
     *
     * @param playerNr   identification of player for which ship will be placed
     * @param shipType   type of ship to be placed
     * @param bowX       x-coordinate of bow
     * @param bowY       y-coordinate of bow
     * @param horizontal indicate whether ship will placed horizontally or vertically
     */
    void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal);

    /**
     * Remove the ship that is placed at the square with coordinates (posX, posY).
     * The state of the ocean area in the player's application will be kept
     * up-to-date by method calls of showSquarePlayer().
     *
     * @param playerNr identification of player for which ship will be removed
     * @param posX     x-coordinate of square where ship was placed
     * @param posY     y-coordinate of square where ship was placed
     */
    void removeShip(int playerNr, int posX, int posY);

    /**
     * Remove all ships that are placed. The state of the ocean area in the
     * player's application will be kept up-to-date by method calls of
     * showSquarePlayer().
     *
     * @param playerNr identification of player for which ships will be removed
     */
    void removeAllShips(int playerNr);

    /**
     * Notify that the player is ready to play the game, i.e., all ships have
     * been placed. When not all ships have been placed, the message
     * "Not all ships have been placed!" will be sent to the player's application
     * by a method call of showErrorMessage().
     *
     * @param playerNr identification of player who is ready to play the game
     */
    void notifyWhenReady(int playerNr);

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
     *  @param playerNr identification of player who fires.
     * @param posX     x-coordinate of square
     * @param posY     y-coordinate of square
     */
    void fireShot(int playerNr, int posX, int posY);

    /**
     * Start a new game. Remove all ships and unregister the player.
     * The ocean area of the player's application will be kept up-to-date by
     * method calls of showSquarePlayer() and the target area of the opponent's
     * application will be kept up-to-date by method calls of showSquareOpponent().
     * In single-player mode also the ships of the opponent will be removed and
     * the target area of the player's application will be updated by method
     * calls of showSquareOpponent().
     *
     * @param playerNr identification of player who starts a new game
     * @param multiPlayer Multi-Player (true) or Single-Player (false) mode ,
     */
    void startNewGame(int playerNr, boolean multiPlayer);

    /**
     * Resets the game and sets all variables back to default.
     */
    void resetGame();
    /**
     * Ends the ongoing game.
     */
    void endGame();

    void setHasPlacedAllShips(boolean value);

    void setPlayerTurn(boolean value);

    void setStarted(boolean value);

    void setPlayerName(String name);
}
