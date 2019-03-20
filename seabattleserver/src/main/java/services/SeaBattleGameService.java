package services;

import domain.*;
import dtos.FireShotResultDto;
import dtos.PlaceShipResultDto;
import dtos.SetReadyResultDto;
import interfaces.IFactoryWithArgument;
import interfaces.ISeaBattleGameService;
import utilities.ShipCreationArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeaBattleGameService implements ISeaBattleGameService {
    private final List<Game> games;
    private final IFactoryWithArgument<Ship, ShipCreationArgument> _shipFactory;

    public SeaBattleGameService(IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory) {
        this._shipFactory = shipFactory;
        games = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public List<Ship> placeShipsAutomatically(int playerNumber) {
        List<Ship> ships = null;
        synchronized(games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    ships = game.placeShipsAutomatically(playerNumber);
                    break;
                }
            }
        }
        return ships;
    }

    @Override
    public PlaceShipResultDto placeShip(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        PlaceShipResultDto placeShipResultDto = new PlaceShipResultDto(null,  null, false);
        synchronized(games) {
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
    public void registerPlayer(Player player) {
        synchronized(games) {
            for (Game game : games) {
                if (game.registerPlayer(player)) {
                    return;
                }
            }
            Game game = new Game();
            game.registerPlayer(player);
            games.add(game);
        }
    }

    @Override
    public FireShotResultDto fireShot(int firingPlayerNumber, int posX, int posY) {
        FireShotResultDto fireShotResultDto = null;
        synchronized(games) {
            for (Game game : games) {
                if (game.containsPlayer(firingPlayerNumber)) {
                    fireShotResultDto = game.fireShot(firingPlayerNumber, posX, posY);
                    break;
                }
            }
        }
        return fireShotResultDto;
    }

    @Override
    public SetReadyResultDto setReady(int playerNumber) {
        SetReadyResultDto setReadyResultDto = null;
        synchronized(games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    setReadyResultDto = game.readyUp(playerNumber);
                    break;
                }
            }
        }
        return setReadyResultDto;
    }
    // TODO: implement game logic and gamestate management
}
