package seabattleservertests;

import common.MessageLogger;
import domain.*;
import domain.interfaces.IFactoryWithArgument;
import dtos.PlaceShipResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlaceShipTests {
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
  public void should_Place_Submarine_Horizontally_On_x2_And_y2_With_No_Other_Ships_Placed_On_The_Correct_Coordinates_Successfully() {
    // Arrange
    List<Point> points = new ArrayList<Point>();

    points.add(new Point(2, 2));
    points.add(new Point(3, 2));
    points.add(new Point(4, 2));

    // Act
    PlaceShipResultDto placeShipResultDto = seaBattleGameService.placeShip(1, ShipType.SUBMARINE, 2, 2, true);

    // Assert
    assertEquals(points.toString(), placeShipResultDto.getShip().getPoints().toString());
  }

  @Test
  public void should_Place_Minesweeper_Vertically_On_x2_And_y2_With_No_Other_Ships_Placed_On_The_Correct_Coordinates_Successfully() {
    // Arrange
    List<Point> points = new ArrayList<Point>();

    points.add(new Point(2, 2));
    points.add(new Point(2, 3));

    // Act
    PlaceShipResultDto placeShipResultDto = seaBattleGameService.placeShip(1, ShipType.MINESWEEPER, 2, 2, false);

    // Assert
    assertEquals(points.toString(), placeShipResultDto.getShip().getPoints().toString());
  }

  @Test
  public void should_Fail_To_Place_Submarine_Horizontally_On_x2_And_y4_With_Minesweeper_Place_On_x2_y3_Vertically() {
    // Arrange
    seaBattleGameService.placeShip(1, ShipType.MINESWEEPER, 2, 3, false);

    // Act
    PlaceShipResultDto placeShipResultDto = seaBattleGameService.placeShip(1, ShipType.SUBMARINE, 2, 4, true);

    // Assert
    assertFalse(placeShipResultDto.isSuccess());
  }

  @Test
  public void should_Fail_To_Place_Submarine_Vertically_On_x2_And_y9_With_No_Other_Ships_Placed() {
    // Act
    PlaceShipResultDto placeShipResultDto = seaBattleGameService.placeShip(1, ShipType.SUBMARINE, 2, 9, false);

    // Assert
    assertFalse(placeShipResultDto.isSuccess());
  }

  @Test
  public void should_Fail_To_Place_Battleship_When_Battleship_Already_Placed() {
    // Arrange
    seaBattleGameService.placeShip(1, ShipType.BATTLESHIP, 2, 3, false);

    // Act
    PlaceShipResultDto placeShipResultDto = seaBattleGameService.placeShip(1, ShipType.BATTLESHIP, 9, 2, true);

    // Assert
    assertFalse(placeShipResultDto.isSuccess());
  }
}
