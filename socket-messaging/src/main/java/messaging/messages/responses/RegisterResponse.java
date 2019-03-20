package messaging.messages.responses;

import messaging.messages.Message;

public class RegisterResponse extends Message {
    public final Integer playerNumber;
    public final Integer opponentPlayerNumber;
    public final String opponentName;

    public RegisterResponse(Integer playerNumber, boolean success, Integer opponentPlayerNumber, String opponentName) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.opponentName = opponentName;
        super.success = success;
    }
}
