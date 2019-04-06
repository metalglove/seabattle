package domain;

public class ShipCreationArgument {
    private final ShipType shipType;
    private final Point point;
    private final boolean horizontal;

    public ShipCreationArgument(ShipType shipType, int bowX, int bowY, boolean horizontal) {
        this.shipType = shipType;
        this.point = new Point(bowX, bowY);
        this.horizontal = horizontal;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
