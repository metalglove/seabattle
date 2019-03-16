package interfaces;

import domain.Player;
import domain.Ship;
import domain.ShipType;

import java.util.List;

public interface ISeaBattleGameService {
    List<Ship> placeShipsAutomatically(int playerNumber);
    Ship placeShip(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal);
    void registerPlayer(Player player);
}
