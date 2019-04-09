package dtos;

public class AddPlayerResultDto {
    private final Integer opponentPlayerNumber;
    private final String opponentName;
    private final boolean success;

    public AddPlayerResultDto(Integer opponentPlayerNumber, String opponentName, boolean success) {
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.opponentName = opponentName;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public Integer getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }
}
