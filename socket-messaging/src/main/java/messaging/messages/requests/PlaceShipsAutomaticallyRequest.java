package messaging.messages.requests;

import messaging.messages.Message;

public class PlaceShipsAutomaticallyRequest extends Message {
    public final int playerNumber;

    public PlaceShipsAutomaticallyRequest(int playerNumber) {

        this.playerNumber = playerNumber;
    }
}