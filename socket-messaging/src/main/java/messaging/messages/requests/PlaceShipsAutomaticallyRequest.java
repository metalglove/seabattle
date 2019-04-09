package messaging.messages.requests;

import messaging.messages.Message;

public class PlaceShipsAutomaticallyRequest extends Message {
  private final int playerNumber;

  public PlaceShipsAutomaticallyRequest(int playerNumber) {

    this.playerNumber = playerNumber;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }
}