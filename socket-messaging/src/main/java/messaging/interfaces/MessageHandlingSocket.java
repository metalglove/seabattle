package messaging.interfaces;

import messaging.sockets.AsyncIdentifiableClientSocket;

import java.nio.ByteBuffer;

public interface MessageHandlingSocket {
    void startReading(AsyncIdentifiableClientSocket client);
    void startHandlingMessage(AsyncIdentifiableClientSocket client, byte[] bytes);
}
