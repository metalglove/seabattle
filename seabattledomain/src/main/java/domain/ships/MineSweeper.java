package domain.ships;

import domain.Point;
import domain.Ship;

public class MineSweeper extends Ship {
  public MineSweeper(Point startingPoint, boolean horizontal) {
    super(startingPoint, horizontal);
    setLength(2);
  }
}
