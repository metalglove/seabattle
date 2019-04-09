package messaging.messages.responses;

import domain.Ship;
import messaging.messages.Message;

import java.util.List;

public class RemoveAllShipsResponse extends Message {
  private final Integer playerNumber;
  private final List<Ship> removedShips;

  public RemoveAllShipsResponse(Integer playerNumber, List<Ship> removedShips, boolean success) {
    this.playerNumber = playerNumber;
    this.removedShips = removedShips;
    super.success = success;
  }

  public List<Ship> getRemovedShips() {
    return removedShips;
  }

  public Integer getPlayerNumber() {
    return playerNumber;
  }
}
