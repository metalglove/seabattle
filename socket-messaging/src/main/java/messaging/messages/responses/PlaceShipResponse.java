package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

import java.util.List;

public class PlaceShipResponse extends Message {
    public final Integer playerNumber;
    public final Ship ship;
    public final Ship shipToRemove;

    public PlaceShipResponse(Integer playerNumber, Ship ship, boolean success, final Ship shipToRemove) {
        this.playerNumber = playerNumber;
        this.ship = ship;
        this.shipToRemove = shipToRemove;
        super.success = success;
    }
}
