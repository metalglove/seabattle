package messaging.messages.requests;

import messaging.messages.Message;

public class RemoveAllShipsRequest extends Message {
    public final int playerNumber;

    public RemoveAllShipsRequest(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
