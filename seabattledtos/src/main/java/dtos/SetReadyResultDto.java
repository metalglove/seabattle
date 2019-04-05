package dtos;

public class SetReadyResultDto {
    private final int playerNumber;
    private final Integer opponentPlayerNumber;
    private final boolean bothReady;

    public SetReadyResultDto(int playerNumber, Integer opponentPlayerNumber, boolean bothReady) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.bothReady = bothReady;
    }

    public boolean isBothReady() {
        return bothReady;
    }

    public Integer getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
