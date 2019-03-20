package messaging.handlers;

import messaging.interfaces.AcceptingSocket;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AcceptingSocket> {
    @Override
    public void completed(AsynchronousSocketChannel result, AcceptingSocket socket) {
        System.out.println("Accepted a new connection!");
        // start accepting the next connection
        socket.startAccepting();

        // register client
        AsyncIdentifiableClientSocket client = new AsyncIdentifiableClientSocket(result);
        // TODO: remove later if not needed anymore
        //socket.registerClient(client);

        // start reading the socket channel for data
        socket.startReading(client);
    }

    @Override
    public void failed(Throwable exc, AcceptingSocket attachment) {
        System.out.println("Failed to accept a new connection!");
        exc.printStackTrace();
    }
}
