package seabattleservertests;

import common.MessageLogger;
import domain.*;
import domain.interfaces.IFactoryWithArgument;
import dtos.RemoveShipResultDto;
import interfaces.ISeaBattleGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SeaBattleGameService;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveShipTests {
    private ISeaBattleGameService seaBattleGameService;
    private IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory;

    @BeforeEach
    public void setUp() {
        shipFactory = new ShipFactory();
        MessageLogger messageLogger = new MessageLogger("GAME-SERVICE");
        seaBattleGameService = new SeaBattleGameService(shipFactory, messageLogger);
        Player player = new Player("Henk", "Karel32", 1);
        seaBattleGameService.registerPlayer(player, false);
        seaBattleGameService.placeShip(1, ShipType.SUBMARINE, 2,2,true);
        seaBattleGameService.placeShip(1, ShipType.BATTLESHIP, 2,3,true);
        seaBattleGameService.placeShip(1, ShipType.MINESWEEPER, 2,4,true);
    }

    @Test
    public void should_Remove_Submarine_On_x2_y2_Successfully(){

        // Act
        RemoveShipResultDto removeShipResultDto = seaBattleGameService.removeShip(1,2,2 );

        // Assert
        assertTrue(removeShipResultDto.isSuccess());
        assertNull(seaBattleGameService.removeShip(1,2,2).getShipToRemove());
    }
    @Test
    public void should_Remove_All_Ships_Individually_On_Each_Point_Successfully(){

        // Act
        RemoveShipResultDto removeShipResultDtoSubmarine = seaBattleGameService.removeShip(1,2,2 );
        RemoveShipResultDto removeShipResultDtoBattleship = seaBattleGameService.removeShip(1,2,3 );
        RemoveShipResultDto removeShipResultDtoMinesweeper = seaBattleGameService.removeShip(1,2,4 );

        // Assert
        assertTrue(removeShipResultDtoSubmarine.isSuccess() && removeShipResultDtoBattleship.isSuccess() && removeShipResultDtoMinesweeper.isSuccess() );
        assertNull(seaBattleGameService.removeShip(1, 2, 2).getShipToRemove());
        assertNull(seaBattleGameService.removeShip(1, 2, 3).getShipToRemove());
        assertNull(seaBattleGameService.removeShip(1, 2, 4).getShipToRemove());
    }

    @Test
    public void should_Fail_To_Remove_Ship_On_x1_y1(){
        // Act
        RemoveShipResultDto removeShipResultDto = seaBattleGameService.removeShip(1,1,1 );

        // Assert
        assertFalse(removeShipResultDto.isSuccess());
    }
}
