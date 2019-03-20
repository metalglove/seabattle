package messaging.messages.responses;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import messaging.messages.Message;

public class OpponentFireShotResponse extends Message {
    public final Integer firingPlayerNumber;
    public final Point point;
    public final ShotType shotType;
    public final Ship ship;

    public OpponentFireShotResponse(Integer firingPlayerNumber, Point point, ShotType shotType, Ship ship, boolean success) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.point = point;
        this.shotType = shotType;
        this.ship = ship;
        super.success = success;
    }
}
