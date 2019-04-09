package domain.actions;

import domain.Ship;
import domain.ShotType;

public class FireShotAction {

  private final Integer playerNumber;
  private final ShotType shotType;
  private final Ship opponentShip;
  private final boolean success;

  public FireShotAction(Integer playerNumber, ShotType shotType, Ship opponentShip, boolean success) {

    this.playerNumber = playerNumber;
    this.shotType = shotType;
    this.opponentShip = opponentShip;
    this.success = success;
  }

  public Ship getOpponentShip() {
    return opponentShip;
  }

  public ShotType getShotType() {
    return shotType;
  }

  public Integer getPlayerNumber() {
    return playerNumber;
  }

  public boolean isSuccess() {
    return success;
  }
}
