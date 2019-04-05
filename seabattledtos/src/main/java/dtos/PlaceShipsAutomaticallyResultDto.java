package dtos;

import domain.Ship;

import java.util.List;

public class PlaceShipsAutomaticallyResultDto
{
    private final List<Ship> shipsToAdd;
    private final List<Ship> shipsToRemove;
    private final boolean success;

    public PlaceShipsAutomaticallyResultDto(final List<Ship> shipsToAdd, final List<Ship> shipsToRemove, final boolean success) {
        this.shipsToAdd = shipsToAdd;
        this.shipsToRemove = shipsToRemove;
        this.success = success;
    }

    public List<Ship> getShipsToRemove() {
        return shipsToRemove;
    }

    public List<Ship> getShipsToAdd() {
        return shipsToAdd;
    }

    public boolean isSuccess() {
        return success;
    }
}
