package seabattleservertests;

import common.MessageLogger;
import domain.*;
import domain.interfaces.IFactoryWithArgument;
import dtos.SetReadyResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetReadyTests {
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
  public void should_Set_Player_1_Ready_Successfully() {
    // Arrange
    seaBattleGameService.placeShip(1, ShipType.MINESWEEPER, 2, 2, true);
    seaBattleGameService.placeShip(1, ShipType.CRUISER, 2, 3, true);
    seaBattleGameService.placeShip(1, ShipType.AIRCRAFTCARRIER, 2, 4, true);
    seaBattleGameService.placeShip(1, ShipType.BATTLESHIP, 2, 5, true);
    seaBattleGameService.placeShip(1, ShipType.SUBMARINE, 2, 6, true);

    // Act
    SetReadyResultDto setReadyResultDto = seaBattleGameService.setReady(1);

    // Assert
    assertTrue(setReadyResultDto.isSuccess());
  }

  @Test
  public void should_Fail_To_Set_Player_1_Ready() {
    // Arrange
    seaBattleGameService.placeShip(1, ShipType.MINESWEEPER, 2, 2, true);
    seaBattleGameService.placeShip(1, ShipType.CRUISER, 2, 3, true);
    seaBattleGameService.placeShip(1, ShipType.AIRCRAFTCARRIER, 2, 4, true);
    seaBattleGameService.placeShip(1, ShipType.BATTLESHIP, 2, 5, true);
    // Missing one ship.

    // Act
    SetReadyResultDto setReadyResultDto = seaBattleGameService.setReady(1);

    // Assert
    assertFalse(setReadyResultDto.isSuccess());
  }
}
