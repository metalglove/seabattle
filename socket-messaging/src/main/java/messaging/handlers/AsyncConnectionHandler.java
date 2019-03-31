package messaging.handlers;

import messaging.utilities.MessageLogger;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncConnectionHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private final MessageLogger messageLogger;

    public AsyncConnectionHandler(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel attachment) {
        messageLogger.info("Connected successfully!");
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        messageLogger.error("Failed to connect! " + exc.getMessage());
    }
}