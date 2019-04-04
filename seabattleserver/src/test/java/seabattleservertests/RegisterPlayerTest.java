package seabattleservertests;

import domain.Player;
import domain.Ship;
import dtos.RegisterPlayerResultDto;
import interfaces.IFactoryWithArgument;
import interfaces.ISeaBattleGameService;
import messaging.utilities.MessageLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;
import utilities.ShipCreationArgument;
import utilities.ShipFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterPlayerTest {
    private ISeaBattleGameService seaBattleGameService;

    @BeforeEach
    public void setUp() {
        IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
    }

    @Test
    public void should_Register_Player_Henk_With_Password_Karel32_To_A_SinglePlayer_Game_When_MultiPlayer_Is_False() {
        // Arrange
        Player player = new Player("Henk", "Karel32", 1);
        boolean multiPlayer = false;

        // Act
        RegisterPlayerResultDto registerPlayerResultDto = seaBattleGameService.registerPlayer(player, multiPlayer);

        // Assert
        assertTrue(registerPlayerResultDto.isSuccess());
        assertEquals(0, (int)registerPlayerResultDto.getOpponentPlayerNumber(), "The opponents ID is higher than 0 and thus is not an AI which rules out it being a SinglePlayer game.");
    }
}