package messaging.messages.responses;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import messaging.messages.Message;

public class FireShotResponse extends Message {
    public final Integer firingPlayerNumber;
    public final ShotType shotType;
    public final Point point;
    public final Ship ship;

    public FireShotResponse(Integer firingPlayerNumber, ShotType shotType, Point point, Ship ship, boolean success) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.shotType = shotType;
        this.point = point;
        this.ship = ship;
        super.success = success;
    }
}
