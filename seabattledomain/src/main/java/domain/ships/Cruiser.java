package domain.ships;

import domain.Point;
import domain.Ship;

public class Cruiser extends Ship {
  public Cruiser(Point startingPoint, boolean horizontal) {
    super(startingPoint, horizontal);
    setLength(3);
  }
}
