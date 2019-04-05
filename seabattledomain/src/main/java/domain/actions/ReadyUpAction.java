package domain.actions;

public class ReadyUpAction {
    private final int playerNumber;
    private final Integer opponentNumber;
    private final boolean bothReady;

    public ReadyUpAction(int playerNumber, Integer opponentNumber, boolean bothReady) {
        this.playerNumber = playerNumber;

        this.opponentNumber = opponentNumber;
        this.bothReady = bothReady;
    }

    public boolean isBothReady() {
        return bothReady;
    }

    public Integer getOpponentNumber() {
        return opponentNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
