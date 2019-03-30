package services;

import domain.*;
import dtos.*;
import interfaces.IFactoryWithArgument;
import interfaces.ISeaBattleGameService;
import utilities.ShipCreationArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class SeaBattleGameService implements ISeaBattleGameService {
    private final List<Game> games;
    private final IFactoryWithArgument<Ship, ShipCreationArgument> _shipFactory;
    private static AtomicLong aiIDCounter = new AtomicLong();

    public SeaBattleGameService(IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory) {
        this._shipFactory = shipFactory;
        games = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public RemoveAllShipsResultDto removeAllShips(int playerNumber) {
        RemoveAllShipsResultDto removeAllShipsResultDto = new RemoveAllShipsResultDto(null, false);

        synchronized (games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    Player player = game.getPlayerFromNumber(playerNumber);

                    final List<Ship> shipsToRemove = player.getShips();
                    for (Ship ship : player.getShips()) {
                        player.removeShip(ship);
                    }
                    removeAllShipsResultDto = new RemoveAllShipsResultDto(shipsToRemove, true);
                    break;
                }
            }
        }
        return removeAllShipsResultDto;
    }

    @Override
    public RemoveShipResultDto removeShip(int playerNumber, int posX, int posY) {
        RemoveShipResultDto removeShipResultDto = new RemoveShipResultDto(null, false);
        Point point = new Point(posX, posY);
        synchronized (games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    Player player = game.getPlayerFromNumber(playerNumber);
                    for (Ship ship : player.getShips()) {
                        if (ship.containsPoint(point)) {
                            removeShipResultDto = new RemoveShipResultDto(ship, true);
                            player.removeShip(ship);
                        }
                    }
                    break;
                }
            }
        }
        return removeShipResultDto;
    }

    @Override
    public PlaceShipsAutomaticallyResultDto placeShipsAutomatically(int playerNumber) {
        PlaceShipsAutomaticallyResultDto placeShipsAutomaticallyResultDto = new PlaceShipsAutomaticallyResultDto(null, null, false);
        synchronized (games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    Player player = game.getPlayerFromNumber(playerNumber);
                    final List<Ship> shipsToRemove = player.getShips();
                    final List<Ship> shipsToAdd = game.placeShipsAutomatically(playerNumber);
                    if(shipsToAdd != null){
                        placeShipsAutomaticallyResultDto = new PlaceShipsAutomaticallyResultDto(shipsToAdd, shipsToRemove, true);
                    }
                    break;
                }
            }
        }
        return placeShipsAutomaticallyResultDto;
    }

    @Override
    public PlaceShipResultDto placeShip(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        PlaceShipResultDto placeShipResultDto = new PlaceShipResultDto(null, null, false, false);
        synchronized (games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    Player player = game.getPlayerFromNumber(playerNumber);
                    Ship ship = _shipFactory.create(new ShipCreationArgument(shipType, bowX, bowY, horizontal));
                    placeShipResultDto = player.addShip(ship);
                    break;
                }
            }
        }
        return placeShipResultDto;
    }

    @Override
    public RegisterPlayerResultDto registerPlayer(Player player, boolean multiPlayer) {
        synchronized (games) {
            if (!multiPlayer) {
                Game game = new Game();
                game.registerPlayer(player);
                int x = (int)aiIDCounter.getAndIncrement() * -1;
                System.out.println("CPU ID: " + x);
                game.registerPlayer(new Player("CPU", "", x));
                game.placeShipsAutomatically(x);
                game.readyUp(x);
                Player opponent = game.getOpponentPlayer(player.getPlayerNumber());
                games.add(game);
                return new RegisterPlayerResultDto(opponent.getPlayerNumber(), opponent.getUsername(), true);
            }
            for (Game game : games) {
                if (game.registerPlayer(player)) {
                    Player opponent = game.getOpponentPlayer(player.getPlayerNumber());
                    return new RegisterPlayerResultDto(opponent.getPlayerNumber(), opponent.getUsername(), true);
                }
            }
        }
        Game game = new Game();
        game.registerPlayer(player);
        games.add(game);
        return new RegisterPlayerResultDto(null, null, true);
    }

    @Override
    public FireShotResultDto fireShot(int firingPlayerNumber, int posX, int posY) {
        FireShotResultDto fireShotResultDto = null;
        synchronized (games) {
            Game gameToRemove = null;
            for (Game game : games) {
                if (game.containsPlayer(firingPlayerNumber)) {
                    fireShotResultDto = game.fireShot(firingPlayerNumber, posX, posY);
                    if (fireShotResultDto.getShotType() == ShotType.ALLSUNK) {
                        gameToRemove = game;
                    }
                    break;
                }
            }
            if (gameToRemove != null) {
                Player player = gameToRemove.getPlayerFromNumber(firingPlayerNumber);
                player.setUnReady();
                Player opponent = gameToRemove.getOpponentPlayer(firingPlayerNumber);
                opponent.setUnReady();
                games.remove(gameToRemove);
            }
        }
        return fireShotResultDto;
    }

    @Override
    public SetReadyResultDto setReady(int playerNumber) {
        SetReadyResultDto setReadyResultDto = null;
        synchronized (games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    setReadyResultDto = game.readyUp(playerNumber);
                    break;
                }
            }
        }
        return setReadyResultDto;
    }
}
