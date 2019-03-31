package messaging.handlers;

import messaging.interfaces.CrashHandler;
import messaging.interfaces.MessageHandlingSocket;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageLogger;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncReadForHandlingMessageHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsyncIdentifiableClientSocket client;
    private final MessageHandlingSocket server;
    private final CrashHandler crashHandler;
    private final MessageLogger messageLogger;

    public AsyncReadForHandlingMessageHandler(MessageHandlingSocket server, AsyncIdentifiableClientSocket client, CrashHandler crashHandler, MessageLogger messageLogger) {
        this.client = client;
        this.server = server;
        this.crashHandler = crashHandler;
        this.messageLogger = messageLogger;
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
        messageLogger.error(String.format("Player number: %d name: %s crashed.", client.getNumber(), client.getName()));
        crashHandler.handle("Failed to handle message! " + exc.getMessage());
    }
}