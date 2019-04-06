package domain;

import domain.actions.FireShotAction;
import domain.actions.ReadyUpAction;
import domain.ships.*;

import java.util.List;

public class Game {
    private Player player1 = null;
    private Player player2 = null;

    public boolean registerPlayer(Player player) {
        if (player1 == null) {
            player1 = player;
        } else if(player2 == null) {
            player2 = player;
        } else {
            return false;
        }
        return true;
    }

    public List<Ship> placeShipsAutomatically(int playerNr) {
        Player player = getPlayerFromNumber(playerNr);
        if (player == null)
            return null;
        AircraftCarrier aircraftCarrier = new AircraftCarrier(new Point(1, 1), true);
        BattleShip battleShip = new BattleShip(new Point(1, 2), true);
        Cruiser cruiser = new Cruiser(new Point(1, 3), true);
        MineSweeper mineSweeper = new MineSweeper(new Point(1, 4), true);
        Submarine submarine = new Submarine(new Point(1, 5), true);
        // TODO: random placement?
        player.addShip(aircraftCarrier);
        player.addShip(battleShip);
        player.addShip(cruiser);
        player.addShip(mineSweeper);
        player.addShip(submarine);
        return player.getShips();
    }

    public Player getPlayerFromNumber(int playerNr) {
        if (containsPlayer(playerNr))
            return player1.getPlayerNumber() == playerNr ? player1 : player2;
        return null;
    }

    public boolean containsPlayer(int playerNumber) {
        if (player1 != null) {
            if (player1.getPlayerNumber().equals(playerNumber))
                return true;
        }
        if (player2 != null) {
            return player2.getPlayerNumber().equals(playerNumber);
        }
        return false;
    }
    public FireShotAction fireShot(int firingPlayerNumber, int x, int y) {
        Player opponent = getOpponentPlayer(firingPlayerNumber);
        if (opponent == null)
            return null;

        Point point = new Point(x, y);
        FireShotAction fireShotAction = new FireShotAction(opponent.getPlayerNumber(), ShotType.MISSED, null, true);
        Ship shipToRemoveIfNeeded = null;
        for (Ship opponentShip : opponent.getShips()) {
            if (opponentShip.containsPoint(point)) {
                if (opponentShip.getLength() == 1) {
                    shipToRemoveIfNeeded = opponentShip;
                    fireShotAction = new FireShotAction(opponent.getPlayerNumber(), ShotType.SUNK, opponentShip, true);
                } else {
                    fireShotAction = new FireShotAction(opponent.getPlayerNumber(), ShotType.HIT, opponentShip, true);
                }
                opponentShip.removePoint(point);
                break;
            }
        }
        if (shipToRemoveIfNeeded != null)
            opponent.removeShip(shipToRemoveIfNeeded);
        if (opponent.getShips().size() == 0)
            fireShotAction = new FireShotAction(opponent.getPlayerNumber(), ShotType.ALLSUNK, shipToRemoveIfNeeded, true);
        return fireShotAction;
    }

    public Player getOpponentPlayer(int otherPlayerNumber) {
        if (containsPlayer(otherPlayerNumber))
            return player1.getPlayerNumber() == otherPlayerNumber ? player2 : player1;
        return null;
    }

    public ReadyUpAction readyUp(int playerNumber) {
        Player player = getPlayerFromNumber(playerNumber);
        if (player == null) {
            return null;
        }
        player.setReady();
        Player opponent = getOpponentPlayer(playerNumber);
        Integer opponentNumber = null;
        boolean bothReady = false;
        if (opponent != null) {
            opponentNumber = opponent.getPlayerNumber();
            if (player.isReady() && opponent.isReady())
                bothReady = true;
        }

        return new ReadyUpAction(playerNumber, opponentNumber, bothReady);
    }
}
