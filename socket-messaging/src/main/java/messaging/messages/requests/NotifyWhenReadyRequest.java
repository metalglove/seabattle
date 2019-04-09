package messaging.messages.requests;

import messaging.messages.Message;

public class NotifyWhenReadyRequest extends Message {
  private final int playerNumber;

  public NotifyWhenReadyRequest(int playerNumber) {
    this.playerNumber = playerNumber;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }
}