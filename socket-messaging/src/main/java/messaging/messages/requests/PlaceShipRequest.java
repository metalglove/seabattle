package messaging.messages.requests;

import domain.ShipType;
import messaging.messages.Message;

public class PlaceShipRequest extends Message {
  private final int playerNumber;
  private final ShipType shipType;
  private final int bowX;
  private final int bowY;
  private final boolean horizontal;

  public PlaceShipRequest(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal) {
    this.playerNumber = playerNumber;
    this.shipType = shipType;
    this.bowX = bowX;
    this.bowY = bowY;
    this.horizontal = horizontal;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }

  public ShipType getShipType() {
    return shipType;
  }

  public int getBowX() {
    return bowX;
  }

  public int getBowY() {
    return bowY;
  }

  public boolean isHorizontal() {
    return horizontal;
  }
}