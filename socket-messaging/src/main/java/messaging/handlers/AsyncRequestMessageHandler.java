package messaging.handlers;

import common.MessageLogger;
import messaging.interfaces.WritingSocket;
import messaging.messages.Message;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncRequestMessageHandler implements CompletionHandler<Message, Message> {
  private final WritingSocket server;
  private final AsyncIdentifiableClientSocket client;
  private final MessageLogger messageLogger;

  public AsyncRequestMessageHandler(WritingSocket server, AsyncIdentifiableClientSocket client, MessageLogger messageLogger) {
    this.server = server;
    this.client = client;
    this.messageLogger = messageLogger;
  }

  @Override
  public void completed(Message response, Message request) {
    try {
      messageLogger.info(format("RequestMessage executed successfully: {%s}, {%s} by {%s}", request.getClass().getSimpleName(), response.getClass().getSimpleName(), client.getChannel().getRemoteAddress().toString()));
    } catch (IOException e) {
      messageLogger.error("Failed to read message! " + e.getMessage());
    }
    server.startWriting(client, response);
  }

  @Override
  public void failed(Throwable exc, Message request) {
    try {
      messageLogger.info(format("RequestMessage executed unsuccessfully: {%s} by {%s}", request.getClass().getSimpleName(), client.getChannel().getRemoteAddress().toString()));
    } catch (IOException e) {
      messageLogger.error("Failed to read message! " + e.getMessage());
    }
  }
}