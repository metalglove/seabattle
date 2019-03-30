package interfaces;

import domain.Player;
import domain.ShipType;
import dtos.*;

public interface ISeaBattleGameService {
    PlaceShipsAutomaticallyResultDto placeShipsAutomatically(int playerNumber);
    RemoveShipResultDto removeShip(int playerNumber, int posX, int posY);
    RemoveAllShipsResultDto removeAllShips(int playerNumber);
    PlaceShipResultDto placeShip(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal);
    RegisterPlayerResultDto registerPlayer(Player player, boolean multiPlayer);
    FireShotResultDto fireShot(int firingPlayerNumber, int posX, int posY);
    SetReadyResultDto setReady(int playerNumber);

}
