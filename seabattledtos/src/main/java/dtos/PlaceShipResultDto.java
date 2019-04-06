package dtos;

import domain.Ship;

public class PlaceShipResultDto {
    private final Ship ship;
    private final Ship oldShip;
    private final boolean hasPlacedAllShips;
    private final boolean success;

    public PlaceShipResultDto(final Ship ship, final Ship oldShip, boolean hasPlacedAllShips, final boolean success) {
        this.ship = ship;
        this.oldShip = oldShip;
        this.hasPlacedAllShips = hasPlacedAllShips;
        this.success = success;
    }

    public Ship getOldShip() {
        return oldShip;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean getHasPlacedAllShips() {
        return hasPlacedAllShips;
    }
}
