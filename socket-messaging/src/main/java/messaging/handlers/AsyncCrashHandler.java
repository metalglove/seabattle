package messaging.handlers;

import common.MessageLogger;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.interfaces.CrashHandler;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class AsyncCrashHandler implements CrashHandler {
    private final ClientAwareWritingSocket server;
    private final AsyncIdentifiableClientSocket client;
    private final MessageLogger messageLogger;

    public AsyncCrashHandler(ClientAwareWritingSocket server, AsyncIdentifiableClientSocket client, MessageLogger messageLogger) {
        this.server = server;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(String errorMessage) {
        messageLogger.error(errorMessage);
        if (client.getNumber() != null)
            server.unRegisterClient(client);
    }
}
