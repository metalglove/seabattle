package messaging.messages.requests;

import messaging.messages.Message;

public class FireShotRequest extends Message {
  private final int firingPlayerNumber;
  private final int posX;
  private final int posY;

  public FireShotRequest(int firingPlayerNumber, int posX, int posY) {
    this.firingPlayerNumber = firingPlayerNumber;
    this.posX = posX;
    this.posY = posY;
  }

  public int getFiringPlayerNumber() {
    return firingPlayerNumber;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }
}