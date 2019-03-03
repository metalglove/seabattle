package messaging.interfaces;

import messaging.messages.Message;

import java.util.List;

public interface ReadableSocket {
    void startReading();
    void addMessage(Message message);
    List<Message> getMessages();
}
