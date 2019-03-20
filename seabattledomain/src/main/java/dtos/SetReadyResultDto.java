package dtos;

public class SetReadyResultDto {
    private final int playerNumber;
    private final int opponentPlayerNumber;
    private final boolean bothReady;

    public SetReadyResultDto(int playerNumber, int opponentPlayerNumber, boolean bothReady) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.bothReady = bothReady;
    }

    public boolean isBothReady() {
        return bothReady;
    }

    public int getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
