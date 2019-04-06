package seabattleservertests;

import common.MessageLogger;
import domain.*;
import domain.interfaces.IFactoryWithArgument;
import dtos.PlaceShipsAutomaticallyResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceShipsAutomaticallyTests {
    private ISeaBattleGameService seaBattleGameService;

    @BeforeEach
    public void setUp() {
        IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
        Player player = new Player("Henk", "Karel32", 1);
        seaBattleGameService.registerPlayer(player, false);
    }

    @Test
    public void should_Place_Ships_Automatically_Successfully(){
        // Act
        PlaceShipsAutomaticallyResultDto placeShipsAutomaticallyResultDto = seaBattleGameService.placeShipsAutomatically(1);

        // Assert
        assertTrue(placeShipsAutomaticallyResultDto.isSuccess());
    }

    @Test
    public void should_Remove_Ships_Automatically_Then_Place_Ships_Automatically_Successfully(){
        // Arrange
        List<Ship> oldShipPlacement = new ArrayList<>();
        oldShipPlacement.add(seaBattleGameService.placeShip(1, ShipType.MINESWEEPER,2,2,false).getShip());
        oldShipPlacement.add(seaBattleGameService.placeShip(1, ShipType.SUBMARINE,3,2,false).getShip());
        oldShipPlacement.add(seaBattleGameService.placeShip(1, ShipType.BATTLESHIP,4,2,false).getShip());

        // Act
        PlaceShipsAutomaticallyResultDto placeShipsAutomaticallyResultDto = seaBattleGameService.placeShipsAutomatically(1);

        // Assert
        assertEquals(oldShipPlacement.toString(), placeShipsAutomaticallyResultDto.getShipsToRemove().toString());
        assertNotEquals(placeShipsAutomaticallyResultDto.getShipsToAdd().toString(), oldShipPlacement.toString());
    }
}
