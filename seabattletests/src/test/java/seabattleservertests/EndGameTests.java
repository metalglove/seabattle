package seabattleservertests;

import common.MessageLogger;
import domain.Player;
import domain.Ship;
import domain.ShipCreationArgument;
import domain.ShipFactory;
import domain.interfaces.IFactoryWithArgument;
import dtos.EndgameResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EndGameTests {
    private ISeaBattleGameService seaBattleGameService;
    private IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory;

    @BeforeEach
    public void setUp() {
        shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
        Player player = new Player("Henk", "Karel32", 1);
        seaBattleGameService.registerPlayer(player, false);
    }

    @Test
    public void should_End_Game_When_Game_Exists_Successfully(){
        // Act
        EndgameResultDto endgameResultDto = seaBattleGameService.endGame(1);

        // Assert
        assertTrue(endgameResultDto.isSuccess());
    }

    @Test
    public void should_Fail_To_End_Game_When_Game_Does_Not_Exist(){
        // Arrange
        seaBattleGameService.endGame(1);

        // Act
        EndgameResultDto endgameResultDto = seaBattleGameService.endGame(1);

        // Assert
        assertFalse(endgameResultDto.isSuccess());
    }
}
