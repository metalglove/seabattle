package messaging.messages.responses;

import messaging.messages.Message;

public class StartNewGameResponse extends Message {
    private final Integer playerNumber;
    private final String opponentName;
    private final Integer opponentPlayerNumber;

    public StartNewGameResponse(Integer playerNumber, boolean success, String opponentName, Integer opponentPlayerNumber) {
        this.playerNumber = playerNumber;
        this.opponentName = opponentName;
        this.opponentPlayerNumber = opponentPlayerNumber;
        super.success = success;
    }

    public Integer getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }
}
