package messaging.interfaces;

import messaging.messages.Message;

public interface WritableSocket {
    void startWriting(Message message);
}
