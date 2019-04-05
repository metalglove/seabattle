package messaging.messages.responses;

import domain.Point;
import domain.Ship;
import domain.ShotType;
import messaging.messages.Message;

public class FireShotResponse extends Message {
    private final Integer firingPlayerNumber;
    private final ShotType shotType;
    private final Point point;
    private final Ship ship;

    public FireShotResponse(Integer firingPlayerNumber, ShotType shotType, Point point, Ship ship, boolean success) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.shotType = shotType;
        this.point = point;
        this.ship = ship;
        super.success = success;
    }

    public Integer getFiringPlayerNumber() {
        return firingPlayerNumber;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public Point getPoint() {
        return point;
    }

    public Ship getShip() {
        return ship;
    }
}
