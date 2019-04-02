package messaging.messages.responses;

import messaging.messages.Message;

public class RegisterResponse extends Message {
    private final Integer playerNumber;
    private final Integer opponentPlayerNumber;
    private final String opponentName;

    public RegisterResponse(Integer playerNumber, boolean success, Integer opponentPlayerNumber, String opponentName) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.opponentName = opponentName;
        super.success = success;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public Integer getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public boolean getSuccess() {
        return super.success;
    }
}
