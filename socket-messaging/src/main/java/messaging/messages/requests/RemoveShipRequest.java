package messaging.messages.requests;

import messaging.messages.Message;

public class RemoveShipRequest extends Message {
    public final int playerNumber;
    public final int posX;
    public final int posY;

    public RemoveShipRequest(int playerNumber, int posX, int posY) {

        this.playerNumber = playerNumber;
        this.posX = posX;
        this.posY = posY;
    }
}
