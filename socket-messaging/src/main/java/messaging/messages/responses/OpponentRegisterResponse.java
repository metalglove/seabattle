package messaging.messages.responses;

import messaging.messages.Message;

public class OpponentRegisterResponse extends Message {
    private final String opponentName;
    private final int opponentNumber;

    public OpponentRegisterResponse(String opponentName, int opponentNumber, boolean success) {
        this.opponentName = opponentName;
        this.opponentNumber = opponentNumber;
        super.success = success;
    }

    public int getOpponentNumber() {
        return opponentNumber;
    }

    public String getOpponentName() {
        return opponentName;
    }
}
