package messaging.messages.commands;

import messaging.messages.Message;

public class RegisterCommand extends Message {
    public final String playername;
    public final String password;
    public final boolean singlePlayerMode;

    public RegisterCommand(String playername, String password, boolean singlePlayerMode) {
        this.playername = playername;
        this.password = password;
        this.singlePlayerMode = singlePlayerMode;
    }
}
