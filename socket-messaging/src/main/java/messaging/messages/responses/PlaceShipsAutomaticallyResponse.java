package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

import java.util.List;

public class PlaceShipsAutomaticallyResponse extends Message {
    public final int playerNumber;
    public final List<Ship> ships;

    public PlaceShipsAutomaticallyResponse(int playerNumber, List<Ship> ships, boolean success) {
        this.playerNumber = playerNumber;
        this.ships = ships;
        super.success = success;
    }
}
