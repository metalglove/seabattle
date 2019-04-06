package domain;

import common.MessageLogger;
import domain.actions.FireShotAction;
import domain.actions.ReadyUpAction;
import domain.interfaces.IFactoryWithArgument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Game {
    private final MessageLogger messageLogger;
    private Player player1 = null;
    private Player player2 = null;
    private final static Random random = new Random();
    private final IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory;

    public Game(IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory, MessageLogger messageLogger) {
        this.shipFactory = shipFactory;
        this.messageLogger = messageLogger;
    }

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
        List<Ship> ships = getRandomShipsPlacement();
        messageLogger.info("Placing ships automatically for [ID: " + player.getPlayerNumber() +", NAME: " + player.getUsername() + "]");
        messageLogger.list(ships);
        ships.forEach(player::addShip);
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

    private static boolean areShipPlacementsValid(List<Ship> ships) {
        HashSet<String> points = new HashSet<>();
        return ships.stream()
                .flatMap(ship -> ship.getPoints().stream())
                .allMatch(point -> points.add(point.toString()));
    }
    private List<Ship> getRandomShipsPlacement() {
        List<Ship> ships = new ArrayList<>();
        placeRandomShip(ships, ShipType.AIRCRAFTCARRIER, 5);
        placeRandomShip(ships, ShipType.BATTLESHIP, 6);
        placeRandomShip(ships, ShipType.SUBMARINE, 7);
        placeRandomShip(ships, ShipType.CRUISER, 7);
        placeRandomShip(ships, ShipType.MINESWEEPER, 8);
        return ships;
    }
    private void placeRandomShip(List<Ship> ships, ShipType shipType, int bound) {
        Ship ship = getRandomShipPlacement(shipType, bound);
        ships.add(ship);
        while (!areShipPlacementsValid(ships)) {
            ships.remove(ship);
            ship = getRandomShipPlacement(shipType, bound);
            ships.add(ship);
        }
    }
    private Ship getRandomShipPlacement(ShipType shipType, int bound) {
        boolean horizontal = random.nextBoolean();
        int x, y;
        if (horizontal) {
            x = random.nextInt(bound);
            y = random.nextInt(10);
        } else {
            x = random.nextInt(10);
            y = random.nextInt(bound);
        }
        return shipFactory.create(new ShipCreationArgument(shipType, x, y, horizontal));
    }
}
