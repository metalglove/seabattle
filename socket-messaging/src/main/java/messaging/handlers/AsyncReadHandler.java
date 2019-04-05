package messaging.handlers;

import messaging.interfaces.ReadableSocket;
import messaging.messages.Message;
import messaging.utilities.MessageConverter;
import messaging.utilities.MessageLogger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final ReadableSocket readableSocket;
    private final MessageLogger messageLogger;

    public AsyncReadHandler(ReadableSocket readableSocket, MessageLogger messageLogger) {
        this.readableSocket = readableSocket;
        this.messageLogger = messageLogger;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        readableSocket.startReading();
        messageLogger.info("Successfully read message!");
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try {
            Message message = MessageConverter.convertFromBytes(bytes);
            readableSocket.addMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            messageLogger.error("Failed to read message (while converting)! " + e.getMessage());
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        messageLogger.error("Failed to read message! " + exc.getMessage());
    }
}