package messaging.messages.responses;

import messaging.messages.Message;

public class StartNewGameResponse extends Message {
    public final Integer playerNumber;

    public StartNewGameResponse(Integer playerNumber, boolean success) {
        this.playerNumber = playerNumber;
        super.success = success;
    }
}
