package messaging.messages.requests;

import messaging.messages.Message;

public class StartNewGameRequest extends Message {
    public final int playerNumber;

    public StartNewGameRequest(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
