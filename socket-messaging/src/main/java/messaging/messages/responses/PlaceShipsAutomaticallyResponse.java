package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

import java.util.List;

public class PlaceShipsAutomaticallyResponse extends Message {
    public final int playerNumber;
    public final List<Ship> shipsToAdd;
    public final List<Ship> shipsToRemove;

    public PlaceShipsAutomaticallyResponse(int playerNumber, List<Ship> shipsToAdd, List<Ship> shipsToRemove, boolean success) {
        this.playerNumber = playerNumber;
        this.shipsToAdd = shipsToAdd;
        this.shipsToRemove = shipsToRemove;
        super.success = success;
    }
}
