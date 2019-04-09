package messaging.messages.responses;

import messaging.messages.Message;

public class NotifyWhenReadyResponse extends Message {
  private final Integer playerNumber;
  private final boolean isPlayersTurn;

  public NotifyWhenReadyResponse(Integer playerNumber, boolean success, boolean isPlayersTurn) {
    this.playerNumber = playerNumber;
    this.isPlayersTurn = isPlayersTurn;
    super.success = success;
  }

  public boolean isPlayersTurn() {
    return isPlayersTurn;
  }

  public Integer getPlayerNumber() {
    return playerNumber;
  }
}
