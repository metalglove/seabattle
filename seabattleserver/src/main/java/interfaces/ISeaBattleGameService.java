package interfaces;

import domain.Player;
import domain.Ship;
import domain.ShipType;
import dtos.FireShotResultDto;
import dtos.PlaceShipResultDto;
import dtos.RegisterPlayerResultDto;
import dtos.SetReadyResultDto;

import java.util.List;

public interface ISeaBattleGameService {
    List<Ship> placeShipsAutomatically(int playerNumber);
    List<Ship> removeAllShips(int playerNumber);
    PlaceShipResultDto placeShip(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal);
    RegisterPlayerResultDto registerPlayer(Player player);
    FireShotResultDto fireShot(int firingPlayerNumber, int posX, int posY);
    SetReadyResultDto setReady(int playerNumber);
}
