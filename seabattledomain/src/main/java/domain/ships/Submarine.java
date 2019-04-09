package domain.ships;

import domain.Point;
import domain.Ship;

public class Submarine extends Ship {
  public Submarine(Point startingPoint, boolean horizontal) {
    super(startingPoint, horizontal);
    setLength(3);
  }
}
