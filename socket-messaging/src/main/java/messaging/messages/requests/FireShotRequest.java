package messaging.messages.requests;

import messaging.messages.Message;

public class FireShotRequest extends Message {
    public final int firingPlayerNumber;
    public final int posX;
    public final int posY;

    public FireShotRequest(int firingPlayerNumber, int posX, int posY) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.posX = posX;
        this.posY = posY;
    }
}