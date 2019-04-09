package messaging.messages.requests;

import messaging.messages.Message;

public class StartNewGameRequest extends Message {
  private final int playerNumber;
  private final boolean multiPlayer;

  public StartNewGameRequest(int playerNumber, boolean multiPlayer) {
    this.playerNumber = playerNumber;
    this.multiPlayer = multiPlayer;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }

  public boolean isMultiPlayer() {
    return multiPlayer;
  }
}
