package messaging.handlers;

import common.MessageLogger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncConnectionHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
  private final MessageLogger messageLogger;

  public AsyncConnectionHandler(MessageLogger messageLogger) {
    this.messageLogger = messageLogger;
  }

  @Override
  public void completed(Void result, AsynchronousSocketChannel attachment) {
    try {
      SocketAddress remoteAddress = attachment.getRemoteAddress();
      messageLogger.info(format("Connected to %s successfully!", remoteAddress));
    } catch (IOException e) {
      messageLogger.info("Failed to connect! " + e.getMessage());
    }
  }

  @Override
  public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
    messageLogger.error("Failed to connect! " + exc.getMessage());
  }
}