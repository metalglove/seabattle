package messaging.handlers;

import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageLogger;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncWriteBufferHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsyncIdentifiableClientSocket client;
    private final MessageLogger messageLogger;

    public AsyncWriteBufferHandler(AsyncIdentifiableClientSocket client, MessageLogger messageLogger) {
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(attachment.hasRemaining()){
            client.getChannel().write(attachment, attachment, this);
        } else {
            messageLogger.info("Successfully sent ResponseMessage!");
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        messageLogger.info("Failed to send ResponseMessage! " + exc.getMessage());
    }
}
