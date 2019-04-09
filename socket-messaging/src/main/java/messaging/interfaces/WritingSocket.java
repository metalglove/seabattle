package messaging.interfaces;

import messaging.messages.Message;
import messaging.sockets.AsyncIdentifiableClientSocket;

public interface WritingSocket {
  void startWriting(AsyncIdentifiableClientSocket client, Message message);
}
