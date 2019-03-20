package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Ship implements Serializable {
    private final Point startingPoint;
    private final boolean horizontal;
    private List<Point> points = new ArrayList<>();

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

    public boolean containsPoint(Point point) {
        for(Point o : points) {
            if(o.equals(point)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWithinBounds() {
        return points.stream().noneMatch(point -> !boundCheck(point.getX()) || !boundCheck(point.getY()));
    }

    private boolean boundCheck(int bound) {
        return bound >= 0 && bound <= 9;
    }
    public boolean isHorizontal() {
        return horizontal;
    }

    public Point getStartingPoint() {
        return startingPoint;
    }

    public void removePoint(Point point) {
        points.remove(point);
    }
}
