package messaging.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected boolean success;

    public boolean isSuccess() {
        return success;
    }
    // TODO: check if something is needed to verify Message legitness xD // public String Data;
}
