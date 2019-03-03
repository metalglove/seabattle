package messaging.sockets;

import messaging.handlers.AsyncConnectionHandler;
import messaging.interfaces.ReadableSocket;
import messaging.interfaces.WritableSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public abstract class AsyncClientSocket implements ReadableSocket, WritableSocket {
    protected final AsynchronousSocketChannel channel;
    private final String host;
    private final int port;

    public AsyncClientSocket(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        channel = AsynchronousSocketChannel.open();
    }

    public AsyncClientSocket(AsynchronousSocketChannel channel) throws IOException {
        this.channel = channel;
        InetSocketAddress address = ((InetSocketAddress) channel.getRemoteAddress());
        host = address.getAddress().getHostAddress();
        port = address.getPort();
    }

    public void connect() {
        channel.connect(new InetSocketAddress(host, port), channel, new AsyncConnectionHandler());
    }
}