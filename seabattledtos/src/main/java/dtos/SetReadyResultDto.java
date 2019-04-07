package dtos;

public class SetReadyResultDto {
    private final Integer playerNumber;
    private final Integer opponentPlayerNumber;
    private final boolean bothReady;
    private final boolean success;

    public SetReadyResultDto(Integer playerNumber, Integer opponentPlayerNumber, boolean bothReady, boolean success) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.bothReady = bothReady;
        this.success = success;
    }

    public boolean isBothReady() {
        return bothReady;
    }

    public Integer getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public boolean isSuccess() {
        return success;
    }
}
