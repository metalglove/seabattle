package seabattleservertests;

import common.MessageLogger;
import domain.Player;
import domain.Ship;
import domain.ShipCreationArgument;
import domain.ShipFactory;
import domain.interfaces.IFactoryWithArgument;
import dtos.RegisterPlayerResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterPlayerTests {
    private ISeaBattleGameService seaBattleGameService;

    @BeforeEach
    public void setUp() {
        IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
    }

    //TODO: More register tests needed?
    @Test
    public void should_Register_Player_Henk_With_Password_Karel32_To_A_SinglePlayer_Game_When_MultiPlayer_Is_False() {
        // Arrange
        Player player = new Player("Henk", "Karel32", 1);
        boolean multiPlayer = false;

        // Act
        RegisterPlayerResultDto registerPlayerResultDto = seaBattleGameService.registerPlayer(player, multiPlayer);

        // Assert
        assertTrue(registerPlayerResultDto.isSuccess());
        assertEquals(-1, (int)registerPlayerResultDto.getOpponentPlayerNumber(), "The opponents ID is higher than 0 and thus is not an AI which rules out it being a SinglePlayer game.");
    }

    @Test
    public void should_Throw_Null_Pointer_Error_When_Registering_Player_Henk_With_Password_Karel32_To_A_MultiPlayer_Game_When_MultiPlayer_Is_True_And_No_Opponent_Registers() {
        // Arrange
        Player player = new Player("Henk", "Karel32", 1);
        boolean multiPlayer = true;

        // Act
        RegisterPlayerResultDto registerPlayerResultDto = seaBattleGameService.registerPlayer(player, multiPlayer);

        // Assert
        assertTrue(registerPlayerResultDto.isSuccess());
        assertNull(registerPlayerResultDto.getOpponentPlayerNumber(), "The opponents ID is not null, which means AI got registered and thus is a SinglePlayer game.");
    }
}