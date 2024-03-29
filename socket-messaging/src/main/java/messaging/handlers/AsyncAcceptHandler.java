package messaging.handlers;

import common.MessageLogger;
import messaging.interfaces.AcceptingSocket;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AcceptingSocket> {
    private final MessageLogger handlerMessageLogger;

    public AsyncAcceptHandler(MessageLogger handlerMessageLogger) {
        this.handlerMessageLogger = handlerMessageLogger;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, AcceptingSocket socket) {
        handlerMessageLogger.info("Accepted a new connection!");
        socket.startAccepting();
        AsyncIdentifiableClientSocket client = new AsyncIdentifiableClientSocket(result);
        socket.startReading(client);
    }

    @Override
    public void failed(Throwable exc, AcceptingSocket attachment) {
        handlerMessageLogger.error("Failed to accept a new connection! " + exc.getMessage());
    }
}
