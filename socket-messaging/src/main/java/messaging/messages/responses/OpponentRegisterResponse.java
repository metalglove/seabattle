package messaging.messages.responses;

import messaging.messages.Message;

public class OpponentRegisterResponse extends Message {
    public final String opponentName;
    public final int opponentNumber;

    public OpponentRegisterResponse(String opponentName, int opponentNumber, boolean success) {
        this.opponentName = opponentName;
        this.opponentNumber = opponentNumber;
        super.success = success;
    }
}
