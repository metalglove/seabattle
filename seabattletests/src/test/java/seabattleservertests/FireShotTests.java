package seabattleservertests;

import common.MessageLogger;
import domain.*;
import domain.interfaces.IFactoryWithArgument;
import dtos.FireShotResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireShotTests {
  private ISeaBattleGameService seaBattleGameService;

  @BeforeEach
  public void setUp() {
    IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory = new ShipFactory();
    MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
    seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
    Player player1 = new Player("Henk", "Karel32", 1);
    Player player2 = new Player("Hank", "Karel32", 2);
    seaBattleGameService.registerPlayer(player1, true);
    seaBattleGameService.registerPlayer(player2, true);
    seaBattleGameService.placeShip(1, ShipType.SUBMARINE, 2, 2, true);
    seaBattleGameService.placeShip(1, ShipType.BATTLESHIP, 2, 3, true);
    seaBattleGameService.placeShip(1, ShipType.MINESWEEPER, 2, 4, true);
    seaBattleGameService.placeShip(1, ShipType.AIRCRAFTCARRIER, 2, 5, true);
    seaBattleGameService.placeShip(1, ShipType.CRUISER, 2, 6, true);
    seaBattleGameService.placeShip(2, ShipType.SUBMARINE, 2, 2, true);
    seaBattleGameService.placeShip(2, ShipType.BATTLESHIP, 2, 3, true);
    seaBattleGameService.placeShip(2, ShipType.MINESWEEPER, 2, 4, true);
    seaBattleGameService.placeShip(2, ShipType.AIRCRAFTCARRIER, 2, 5, true);
    seaBattleGameService.placeShip(2, ShipType.CRUISER, 2, 6, true);
    seaBattleGameService.setReady(1);
    seaBattleGameService.setReady(2);
  }

  @Test
  public void should_Fire_Shot_On_x2_y2_Successfully() {
    // Act
    FireShotResultDto fireShotResultDto = seaBattleGameService.fireShot(1, 2, 2);
    Point point = fireShotResultDto.getPoint();

    // Assert
    assertEquals(point, new Point(2, 2));
  }

  @Test
  public void should_Fire_Shot_On_x2_y2_Hit_Successfully() {
    // Act
    FireShotResultDto fireShotResultDto = seaBattleGameService.fireShot(1, 2, 2);

    // Assert
    assertEquals(ShotType.HIT, fireShotResultDto.getShotType());
  }

  @Test
  public void should_Fire_Shot_On_x2_y8_Missed_Successfully() {
    // Act
    FireShotResultDto fireShotResultDto = seaBattleGameService.fireShot(1, 2, 8);

    // Assert
    assertEquals(ShotType.MISSED, fireShotResultDto.getShotType());
  }

  @Test
  public void should_Fire_Shots_Submarine_Sunk_Successfully() {
    // Arrange
    seaBattleGameService.fireShot(1, 2, 2);
    seaBattleGameService.fireShot(1, 3, 2);

    // Act
    FireShotResultDto fireShotResultDto = seaBattleGameService.fireShot(1, 4, 2);

    // Assert
    assertTrue(fireShotResultDto.getShip().isSunk());
  }

  @Test
  public void should_Fire_Shots_On_All_Ships_AllSunk_Successfully() {
    // Arrange
    seaBattleGameService.fireShot(1, 2, 2);
    seaBattleGameService.fireShot(1, 3, 2);
    seaBattleGameService.fireShot(1, 4, 2);
    seaBattleGameService.fireShot(1, 2, 3);
    seaBattleGameService.fireShot(1, 3, 3);
    seaBattleGameService.fireShot(1, 4, 3);
    seaBattleGameService.fireShot(1, 5, 3);
    seaBattleGameService.fireShot(1, 6, 3);
    seaBattleGameService.fireShot(1, 2, 4);
    seaBattleGameService.fireShot(1, 3, 4);
    seaBattleGameService.fireShot(1, 2, 5);
    seaBattleGameService.fireShot(1, 3, 5);
    seaBattleGameService.fireShot(1, 4, 5);
    seaBattleGameService.fireShot(1, 5, 5);
    seaBattleGameService.fireShot(1, 6, 5);
    seaBattleGameService.fireShot(1, 2, 6);
    seaBattleGameService.fireShot(1, 3, 6);

    // Act
    FireShotResultDto fireShotResultDto = seaBattleGameService.fireShot(1, 4, 6);

    // Assert
    assertEquals(ShotType.ALLSUNK, fireShotResultDto.getShotType());
  }
}
