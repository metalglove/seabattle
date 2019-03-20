package dtos;

import domain.Point;
import domain.ShotType;

public class FireShotResultDto {
    private final Integer firingPlayerNumber;
    private final Integer receivingPlayerNumber;
    private final Point point;
    private final ShotType shotType;

    public FireShotResultDto(Integer firingPlayerNumber, Integer receivingPlayerNumber, Point point, ShotType shotType) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.receivingPlayerNumber = receivingPlayerNumber;
        this.point = point;
        this.shotType = shotType;
    }

    public Integer getFiringPlayerNumber() {
        return firingPlayerNumber;
    }

    public Integer getReceivingPlayerNumber() {
        return receivingPlayerNumber;
    }

    public Point getPoint() {
        return point;
    }

    public ShotType getShotType() {
        return shotType;
    }
}
