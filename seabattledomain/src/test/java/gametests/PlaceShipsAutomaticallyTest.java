package gametests;

import common.MessageLogger;
import domain.Game;
import domain.Player;
import domain.Ship;
import domain.ShipFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PlaceShipsAutomaticallyTest {

    @Test
    public void should_Place_All_Ships() {
        Game game = new Game(new ShipFactory(), new MessageLogger("GAME"));
        game.registerPlayer(new Player("Jan", "asd", 1));
        game.registerPlayer(new Player("Henk", "Kerel32", 2));

        List<Ship> ships = game.placeShipsAutomatically(1);
    }
}
