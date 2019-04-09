package messaging.interfaces;

import messaging.sockets.AsyncIdentifiableClientSocket;

public interface ClientAwareWritingSocket extends WritingSocket {
  AsyncIdentifiableClientSocket getClientById(int id);

  void registerClient(AsyncIdentifiableClientSocket client);

  void unRegisterClient(AsyncIdentifiableClientSocket client);
}
