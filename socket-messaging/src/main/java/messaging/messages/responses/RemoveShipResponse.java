package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

import java.util.List;

public class RemoveShipResponse extends Message {
    public final Integer playerNumber;
    public final Ship ship;

    public RemoveShipResponse(Integer playerNumber, Ship ship, boolean success) {
        this.playerNumber = playerNumber;
        this.ship = ship;
        super.success = success;

    }
}
