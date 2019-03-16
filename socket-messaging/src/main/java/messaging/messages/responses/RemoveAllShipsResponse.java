package messaging.messages.responses;

import messaging.messages.Message;

public class RemoveAllShipsResponse extends Message {
    private final Integer playerNumber;

    public RemoveAllShipsResponse(Integer playerNumber, boolean success) {
        this.playerNumber = playerNumber;
        super.success = success;
    }
}
