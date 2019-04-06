package messaging.interfaces;

import messaging.messages.Message;

public interface ReadableSocket {
    void startReading();
    void addMessage(Message message);
}
