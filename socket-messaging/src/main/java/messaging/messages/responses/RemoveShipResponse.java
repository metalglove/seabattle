package messaging.messages.responses;

import messaging.messages.Message;

public class RemoveShipResponse extends Message {
    public final Integer playerNumber;

    public RemoveShipResponse(Integer playerNumber, boolean success) {
        this.playerNumber = playerNumber;
        super.success = success;
    }
}
