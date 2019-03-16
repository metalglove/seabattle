package services;

import domain.Game;
import domain.Player;
import domain.Ship;
import domain.ShipType;
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
    public Ship placeShip(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        synchronized(games) {
            for (Game game : games) {
                if (game.containsPlayer(playerNumber)) {
                    Player player = game.getPlayerFromNumber(playerNumber);
                    Ship ship = _shipFactory.create(new ShipCreationArgument(shipType, bowX, bowY, horizontal));
                    if (player.addShip(ship)) {
                        return ship;
                    }
                    break;
                }
            }
        }
        return null;
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
    // TODO: implement game logic and gamestate management
}
