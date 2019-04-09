package messaging.messages.responses;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import messaging.messages.Message;

public class OpponentFireShotResponse extends Message {
  private final Integer firingPlayerNumber;
  private final Point point;
  private final ShotType shotType;
  private final Ship ship;

  public OpponentFireShotResponse(Integer firingPlayerNumber, Point point, ShotType shotType, Ship ship, boolean success) {
    this.firingPlayerNumber = firingPlayerNumber;
    this.point = point;
    this.shotType = shotType;
    this.ship = ship;
    super.success = success;
  }

  public Ship getShip() {
    return ship;
  }

  public ShotType getShotType() {
    return shotType;
  }

  public Point getPoint() {
    return point;
  }

  public Integer getFiringPlayerNumber() {
    return firingPlayerNumber;
  }
}
