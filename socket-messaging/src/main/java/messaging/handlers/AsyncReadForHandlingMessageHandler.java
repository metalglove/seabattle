package messaging.handlers;

import messaging.interfaces.MessageHandlingSocket;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncReadForHandlingMessageHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsyncIdentifiableClientSocket client;
    private final MessageHandlingSocket server;

    public AsyncReadForHandlingMessageHandler(MessageHandlingSocket server, AsyncIdentifiableClientSocket client) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        server.startReading(client);
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        server.startHandlingMessage(client, bytes);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
        try {
            client.getChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}