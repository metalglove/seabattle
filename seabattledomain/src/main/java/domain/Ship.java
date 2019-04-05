package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Ship implements Serializable {
    private final Point startingPoint;
    private final boolean horizontal;
    private List<Point> points = new ArrayList<>();
    private List<Point> pointsHit = new ArrayList<>();

    public Ship(Point startingPoint, boolean horizontal) {
        this.startingPoint = startingPoint;
        this.horizontal = horizontal;
    }

    protected void setLength(int length) {
        points.add(startingPoint);
        if (horizontal) {
            for (int i = 1; i < length; i++) {
                points.add(new Point(startingPoint.getX() + i, startingPoint.getY()));
            }
        } else {
            for (int i = 1; i < length; i++) {
                points.add(new Point(startingPoint.getX(), startingPoint.getY() + i));
            }
        }
    }
    public int getLength() {
        return points.size();
    }

    public List<Point> getPoints() {
        return List.copyOf(points);
    }

    public List<Point> getPointsHit() {
        return List.copyOf(pointsHit);
    }

    public boolean containsPoint(Point point) {
        return points.stream().anyMatch(o -> o.equals(point));
    }

    public boolean isWithinBounds() {
        return points.stream().noneMatch(point -> !boundCheck(point.getX()) || !boundCheck(point.getY()));
    }

    public boolean isSunk() {
        return points.size() == 0;
    }

    public void removePoint(Point point) {
        points.remove(point);
        pointsHit.add(point);
    }

    private boolean boundCheck(int bound) {
        return bound >= 0 && bound <= 9;
    }
}
