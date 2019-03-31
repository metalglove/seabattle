package messaging.handlers;

import messaging.utilities.MessageLogger;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncWriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    private final MessageLogger messageLogger;

    public AsyncWriteHandler(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {
        messageLogger.info("Successfully sent message!");
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        messageLogger.error("Failed to send message! " + exc.getMessage());
    }
}