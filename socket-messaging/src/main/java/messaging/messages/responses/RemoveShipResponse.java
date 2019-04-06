package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

public class RemoveShipResponse extends Message {
    private final Integer playerNumber;
    private final Ship ship;

    public RemoveShipResponse(Integer playerNumber, Ship ship, boolean success) {
        this.playerNumber = playerNumber;
        this.ship = ship;
        super.success = success;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public Ship getShip() {
        return ship;
    }
}
