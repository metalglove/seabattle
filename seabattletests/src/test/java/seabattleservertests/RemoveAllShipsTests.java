package seabattleservertests;

import common.MessageLogger;
import domain.*;
import domain.interfaces.IFactoryWithArgument;
import dtos.RemoveAllShipsResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveAllShipsTests {
    private ISeaBattleGameService seaBattleGameService;
    private IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory;

    @BeforeEach
    public void setUp() {
        shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
        Player player = new Player("Henk", "Karel32", 1);
        seaBattleGameService.registerPlayer(player, false);
        seaBattleGameService.placeShip(0, ShipType.SUBMARINE, 2,2,true);
        seaBattleGameService.placeShip(0, ShipType.BATTLESHIP, 2,3,true);
        seaBattleGameService.placeShip(0, ShipType.MINESWEEPER, 2,4,true);
    }
    @Test
    public void should_Remove_All_Ships_Successfully(){
        // Act
        RemoveAllShipsResultDto removeAllShipsResultDto =  seaBattleGameService.removeAllShips(1);

        // Assert
        assertEquals(0, removeAllShipsResultDto.getShipsToRemove().size());
    }
}
