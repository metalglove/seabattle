package messaging.messages.responses;

import messaging.messages.Message;

public class RegisterResponse extends Message {
    private final Integer playerNumber;
    private final Integer opponentPlayerNumber;
    private final String opponentName;
    private final boolean duplicate;

    public RegisterResponse(Integer playerNumber, boolean success, Integer opponentPlayerNumber, String opponentName, boolean duplicate) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.opponentName = opponentName;
        this.duplicate = duplicate;
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

    public boolean getIsDuplicate() {
        return duplicate;
    }
}
