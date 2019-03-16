package messaging.messages.responses;

import domain.ShotType;
import messaging.messages.Message;

public class FireShotResponse extends Message {
    public final Integer firingPlayerNumber;
    public final ShotType shotType;

    public FireShotResponse(Integer firingPlayerNumber, ShotType shotType, boolean success) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.shotType = shotType;
        super.success = success;
    }
}
