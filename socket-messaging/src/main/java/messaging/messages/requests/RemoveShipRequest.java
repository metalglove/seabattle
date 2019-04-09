package messaging.messages.requests;

import messaging.messages.Message;

public class RemoveShipRequest extends Message {
  private final int playerNumber;
  private final int posX;
  private final int posY;

  public RemoveShipRequest(int playerNumber, int posX, int posY) {

    this.playerNumber = playerNumber;
    this.posX = posX;
    this.posY = posY;
  }

  public int getPosY() {
    return posY;
  }

  public int getPosX() {
    return posX;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }
}
