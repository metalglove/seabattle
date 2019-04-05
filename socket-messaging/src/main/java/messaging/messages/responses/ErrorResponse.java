package messaging.messages.responses;

import messaging.messages.Message;

public class ErrorResponse extends Message {
    private final String errorMessage;
    private final String playerName;
    private final Integer playerNumber;

    public ErrorResponse(String errorMessage, String playerName, Integer playerNumber) {
        this.errorMessage = errorMessage;
        this.playerName = playerName;
        this.playerNumber = playerNumber;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
