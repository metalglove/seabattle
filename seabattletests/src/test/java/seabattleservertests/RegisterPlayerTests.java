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
  private IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory;

  @BeforeEach
  public void setUp() {
    shipFactory = new ShipFactory();
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
    assertEquals(0, (int) registerPlayerResultDto.getOpponentPlayerNumber(), "The opponents ID is higher than 0 and thus is not an AI which rules out it being a SinglePlayer game.");
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

  //TODO: Not testable because this is already pre-checked in the RegisterRequestHandler
//    @Test
//    public void should_Fail__When_Registering_Player_Henk_With_Password_Karel32_To_A_MultiPlayer_Game_When_Player_With_Same_Player_Name_Already_Registered() {
//        // Arrange
//        Player player1 = new Player("Henk", "Karel32", 1);
//        Player player2 = new Player("Henk", "Karel32", 1);
//        boolean multiPlayer = true;
//
//        // Act
//        RegisterPlayerResultDto registerPlayerResultDto1 = seaBattleGameService.registerPlayer(player1, multiPlayer);
//        RegisterPlayerResultDto registerPlayerResultDto2 = seaBattleGameService.registerPlayer(player2, multiPlayer);
//
//        // Assert
//        assertTrue(registerPlayerResultDto1.isSuccess());
//        assertFalse(registerPlayerResultDto2.isSuccess());
//    }
}
