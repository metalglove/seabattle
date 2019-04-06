package seabattleservertests;

import common.MessageLogger;
import domain.Player;
import domain.Ship;
import domain.ShipCreationArgument;
import domain.ShipFactory;
import domain.interfaces.IFactoryWithArgument;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import services.SeaBattleGameService;

public class FireShotTests {
    private ISeaBattleGameService seaBattleGameService;

    @BeforeEach
    public void setUp() {
        IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
        Player player = new Player("Henk", "Karel32", 1);
        seaBattleGameService.registerPlayer(player, false);
    }

    //TODO: Add Fire shot tests
}
