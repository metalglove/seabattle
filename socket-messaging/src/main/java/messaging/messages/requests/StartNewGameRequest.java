package messaging.messages.requests;

import messaging.messages.Message;

public class StartNewGameRequest extends Message {
    public final int playerNumber;
    public final boolean multiPlayer;

    public StartNewGameRequest(int playerNumber, boolean multiPlayer) {
        this.playerNumber = playerNumber;
        this.multiPlayer = multiPlayer;
    }
}
