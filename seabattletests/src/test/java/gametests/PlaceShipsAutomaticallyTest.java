package gametests;

import common.MessageLogger;
import domain.Game;
import domain.Player;
import domain.Ship;
import domain.ShipFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaceShipsAutomaticallyTest {

  @Test
  public void should_Place_All_Ships_Test() {
    Game game = new Game(new ShipFactory(), new MessageLogger("GAME"));
    game.registerPlayer(new Player("Jan", 1));
    game.registerPlayer(new Player("Henk", 2));

    List<Ship> ships = game.placeShipsAutomatically(1);

    assertEquals(5, (long) ships.size());
  }
}
