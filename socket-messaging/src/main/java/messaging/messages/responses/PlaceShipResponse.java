package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

public class PlaceShipResponse extends Message {
    private final Integer playerNumber;
    private final Ship ship;
    private final Ship shipToRemove;
    private final boolean hasPlacedAllShips;

    public PlaceShipResponse(Integer playerNumber, Ship ship, boolean success, final Ship shipToRemove, boolean hasPlacedAllShips) {
        this.playerNumber = playerNumber;
        this.ship = ship;
        this.shipToRemove = shipToRemove;
        this.hasPlacedAllShips = hasPlacedAllShips;
        super.success = success;
    }

    public boolean hasPlacedAllShips() {
        return hasPlacedAllShips;
    }

    public Ship getShipToRemove() {
        return shipToRemove;
    }

    public Ship getShip() {
        return ship;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }
}
