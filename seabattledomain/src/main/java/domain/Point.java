package domain;

import java.io.Serializable;

import static java.lang.String.format;

public class Point implements Serializable {
  private final int x;
  private final int y;

  public Point(int x, int y) {

    this.x = x;
    this.y = y;
  }

  public int getY() {
    return y;
  }

  public int getX() {
    return x;
  }

  @Override
  public boolean equals(Object point) {
    if (!(point instanceof Point)) return false;
    return ((Point) point).getX() == x && ((Point) point).getY() == y;
  }

  @Override
  public String toString() {
    return format("{X: %s, Y: %s}", x, y);
  }
}
