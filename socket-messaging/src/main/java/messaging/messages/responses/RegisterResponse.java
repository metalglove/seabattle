package messaging.messages.responses;

import messaging.messages.Message;

public class RegisterResponse extends Message {
    public final Integer playerNumber;

    public RegisterResponse(Integer playerNumber, boolean success) {
        this.playerNumber = playerNumber;
        super.success = success;
    }
}
