package messaging.interfaces;

import messaging.sockets.AsyncIdentifiableClientSocket;

public interface AcceptingSocket {
    void startReading(AsyncIdentifiableClientSocket client);
    void startAccepting();
}
