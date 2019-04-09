package messaging.messages.requests;

import messaging.messages.Message;

public class RemoveAllShipsRequest extends Message {
  private final int playerNumber;

  public RemoveAllShipsRequest(int playerNumber) {
    this.playerNumber = playerNumber;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }
}
