package dtos;

import domain.Point;
import domain.Ship;
import domain.ShotType;

public class FireShotResultDto {
    private final Integer firingPlayerNumber;
    private final Integer receivingPlayerNumber;
    private final Point point;
    private final ShotType shotType;
    private final Ship ship;

    public FireShotResultDto(Integer firingPlayerNumber, Integer receivingPlayerNumber, Point point, ShotType shotType, Ship ship) {
        this.firingPlayerNumber = firingPlayerNumber;
        this.receivingPlayerNumber = receivingPlayerNumber;
        this.point = point;
        this.shotType = shotType;
        this.ship = ship;
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

    public Ship getShip() {
        return ship;
    }
}
