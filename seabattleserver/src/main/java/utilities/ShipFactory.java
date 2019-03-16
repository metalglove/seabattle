package utilities;

import domain.Ship;
import domain.ships.*;
import interfaces.IFactoryWithArgument;

public class ShipFactory implements IFactoryWithArgument<Ship, ShipCreationArgument> {
    @Override
    public Ship create(ShipCreationArgument shipCreationArgument) {
        Ship ship;
        switch (shipCreationArgument.getShipType()) {
            case AIRCRAFTCARRIER:
                ship = new AircraftCarrier(shipCreationArgument.getPoint(), shipCreationArgument.isHorizontal());
                break;
            case BATTLESHIP:
                ship = new BattleShip(shipCreationArgument.getPoint(), shipCreationArgument.isHorizontal());
                break;
            case CRUISER:
                ship = new Cruiser(shipCreationArgument.getPoint(), shipCreationArgument.isHorizontal());
                break;
            case SUBMARINE:
                ship = new Submarine(shipCreationArgument.getPoint(), shipCreationArgument.isHorizontal());
                break;
            case MINESWEEPER:
                ship = new MineSweeper(shipCreationArgument.getPoint(), shipCreationArgument.isHorizontal());
                break;
            default:
                // TODO: log
                ship = null;
                break;
        }
        return ship;
    }
}
