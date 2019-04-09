package messaging.interfaces;

import messaging.sockets.AsyncIdentifiableClientSocket;

public interface MessageHandlingSocket {
  void startReading(AsyncIdentifiableClientSocket client);

  void startHandlingMessage(AsyncIdentifiableClientSocket client, byte[] bytes);
}
