package interfaces;

import messaging.messages.Message;
import messaging.sockets.AsyncIdentifiableClientSocket;

public interface RequestHandler<T extends Message> {
    void handle(T request, AsyncIdentifiableClientSocket client);
}
