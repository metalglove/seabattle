package messaging.messages.responses;

import messaging.messages.Message;

public class LoginResponse extends Message {
    private final Integer playerNumber;
    private final Integer opponentPlayerNumber;
    private final String opponentName;
    private final boolean credentialsCorrect;

    public LoginResponse(Integer playerNumber, boolean success, Integer opponentPlayerNumber, String opponentName, boolean credentialsCorrect) {
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.opponentName = opponentName;
        this.credentialsCorrect = credentialsCorrect;
        super.success = success;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public Integer getOpponentPlayerNumber() {
        return opponentPlayerNumber;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public boolean getCredentialsCorrect() {
        return credentialsCorrect;
    }
}
