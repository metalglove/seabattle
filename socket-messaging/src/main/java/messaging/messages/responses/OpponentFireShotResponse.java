package messaging.messages.responses;

import domain.Point;
import domain.ShotType;
import messaging.messages.Message;

public class OpponentFireShotResponse extends Message {
    public final Integer firingPlayerNumber;
    public final Point point;
    public final ShotType shotType;

    public OpponentFireShotResponse(Integer firingPlayerNumber, Point point, ShotType shotType, boolean success) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.point = point;
        this.shotType = shotType;
        super.success = success;
    }
}
