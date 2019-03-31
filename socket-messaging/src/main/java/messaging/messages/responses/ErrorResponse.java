package messaging.messages.responses;

import messaging.messages.Message;

public class ErrorResponse extends Message {
    public final String errorMessage;
    public final String playerName;
    public final Integer playerNumber;

    public ErrorResponse(String errorMessage, String playerName, Integer playerNumber) {
        this.errorMessage = errorMessage;
        this.playerName = playerName;
        this.playerNumber = playerNumber;
    }
}
