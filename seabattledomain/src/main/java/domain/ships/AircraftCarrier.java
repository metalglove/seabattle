package domain.ships;

import domain.Point;
import domain.Ship;

public class AircraftCarrier extends Ship {
  public AircraftCarrier(Point startingPoint, boolean horizontal) {
    super(startingPoint, horizontal);
    setLength(5);
  }
}
