package interfaces;

import messaging.interfaces.WritingSocket;
import messaging.sockets.AsyncIdentifiableClientSocket;

public interface ClientAwareWritingSocket extends WritingSocket {
    AsyncIdentifiableClientSocket getClientById(int id);
    void registerClient(AsyncIdentifiableClientSocket client);
}
