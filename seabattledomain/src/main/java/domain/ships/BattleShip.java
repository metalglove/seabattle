package domain.ships;

import domain.Point;
import domain.Ship;

public class BattleShip extends Ship {
    public BattleShip(Point startingPoint, boolean horizontal) {
        super(startingPoint, horizontal);
        setLength(4);
    }
}
