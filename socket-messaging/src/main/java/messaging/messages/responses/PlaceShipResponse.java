package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

public class PlaceShipResponse extends Message {
    public final Integer playerNumber;
    public final Ship ship;

    public PlaceShipResponse(Integer playerNumber, Ship ship, boolean success) {
        this.playerNumber = playerNumber;
        this.ship = ship;
        super.success = success;
    }
}
