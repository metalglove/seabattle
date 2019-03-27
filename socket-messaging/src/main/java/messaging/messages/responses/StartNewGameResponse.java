package messaging.messages.responses;

import messaging.messages.Message;

public class StartNewGameResponse extends Message {
    public final Integer playerNumber;
    public final String opponentName;
    public final Integer opponentPlayerNumber;

    public StartNewGameResponse(Integer playerNumber, boolean success, String opponentName, Integer opponentPlayerNumber) {
        this.playerNumber = playerNumber;
        this.opponentName = opponentName;
        this.opponentPlayerNumber = opponentPlayerNumber;
        super.success = success;
    }
}
