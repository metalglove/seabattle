package dtos;

public class EndgameResultDto {
  private final boolean success;
  private final Integer opponentPlayerId;

  public EndgameResultDto(boolean success, Integer opponentPlayerId) {
    this.success = success;
    this.opponentPlayerId = opponentPlayerId;
  }

  public boolean isSuccess() {
    return success;
  }

  public Integer getOpponentPlayerId() {
    return opponentPlayerId;
  }
}
