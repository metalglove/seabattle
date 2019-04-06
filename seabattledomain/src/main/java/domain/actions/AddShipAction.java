package domain.actions;

import domain.Ship;

public class AddShipAction {
    private final Ship oldShip;
    private final boolean hasPlacedAllShips;
    private final boolean success;

    public AddShipAction(Ship oldShip, boolean hasPlacedAllShips, boolean success) {
        this.oldShip = oldShip;
        this.hasPlacedAllShips = hasPlacedAllShips;
        this.success = success;
    }

    public boolean hasPlacedAllShips() {
        return hasPlacedAllShips;
    }

    public Ship getOldShip() {
        return oldShip;
    }

    public boolean isSuccess() {
        return success;
    }
}
