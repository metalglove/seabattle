package dtos;

import domain.Ship;

public class RemoveShipResultDto {

  private final Ship shipToRemove;
  private final boolean success;

  public RemoveShipResultDto(final Ship shipToRemove, final boolean success) {
    this.shipToRemove = shipToRemove;
    this.success = success;
  }

  public Ship getShipToRemove() {
    return shipToRemove;
  }

  public boolean isSuccess() {
    return success;
  }
}
