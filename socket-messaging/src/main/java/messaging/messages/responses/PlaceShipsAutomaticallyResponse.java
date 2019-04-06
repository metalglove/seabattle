package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

import java.util.List;

public class PlaceShipsAutomaticallyResponse extends Message {
    private final int playerNumber;
    private final List<Ship> shipsToAdd;
    private final List<Ship> shipsToRemove;

    public PlaceShipsAutomaticallyResponse(int playerNumber, List<Ship> shipsToAdd, List<Ship> shipsToRemove, boolean success) {
        this.playerNumber = playerNumber;
        this.shipsToAdd = shipsToAdd;
        this.shipsToRemove = shipsToRemove;
        super.success = success;
    }

    public List<Ship> getShipsToRemove() {
        return shipsToRemove;
    }

    public List<Ship> getShipsToAdd() {
        return shipsToAdd;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
