package domain;

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
            System.out.printf("Player: {%s} tried to join a game that is already full!", player.getUsername());
            return false;
        }
        return true;
    }

    public List<Ship> placeShipsAutomatically(int playerNr) {
        Player player = getPlayerFromNumber(playerNr);
        AircraftCarrier aircraftCarrier = new AircraftCarrier(new Point(1, 1), true);
        BattleShip battleShip = new BattleShip(new Point(1, 2), true);
        Cruiser cruiser = new Cruiser(new Point(1, 3), true);
        MineSweeper mineSweeper = new MineSweeper(new Point(1, 4), true);
        Submarine submarine = new Submarine(new Point(1, 5), true);
        player.addShip(aircraftCarrier);
        player.addShip(battleShip);
        player.addShip(cruiser);
        player.addShip(mineSweeper);
        player.addShip(submarine);
        return player.getShips();
    }

    private Player getPlayerFromNumber(int playerNr) {
        return player1.getPlayerNumber() == playerNr ? player1 : player2;
    }
}
