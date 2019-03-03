package messaging.handlers;

import messaging.sockets.AsyncIdentifiableClientSocket;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncWriteBufferHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsyncIdentifiableClientSocket client;

    public AsyncWriteBufferHandler(AsyncIdentifiableClientSocket client) {
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(attachment.hasRemaining()){
            client.getChannel().write(attachment, attachment, this);
        } else {
            System.out.println("Successfully sent ResponseMessage!");
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("Failed to send ResponseMessage!");
        exc.printStackTrace();
    }
}
