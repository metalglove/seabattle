package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

public class PlaceShipResponse extends Message {
    public final Integer playerNumber;
    public final Ship ship;
    public final Ship shipToRemove;
    public final boolean hasPlacedAllShips;

    public PlaceShipResponse(Integer playerNumber, Ship ship, boolean success, final Ship shipToRemove, boolean hasPlacedAllShips) {
        this.playerNumber = playerNumber;
        this.ship = ship;
        this.shipToRemove = shipToRemove;
        this.hasPlacedAllShips = hasPlacedAllShips;
        super.success = success;
    }
}
