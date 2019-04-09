package dtos;

import domain.Ship;

import java.util.List;

public class RemoveAllShipsResultDto {

  private final List<Ship> shipsToRemove;
  private final boolean success;

  public RemoveAllShipsResultDto(final List<Ship> shipsToRemove, final boolean success) {
    this.shipsToRemove = shipsToRemove;
    this.success = success;
  }

  public List<Ship> getShipsToRemove() {
    return shipsToRemove;
  }

  public boolean isSuccess() {
    return success;
  }
}
