package seabattleserver;

import messaging.handlers.*;
import messaging.interfaces.AcceptingSocket;
import messaging.interfaces.MessageHandlingSocket;
import messaging.interfaces.WritingSocket;
import messaging.messages.Message;
import messaging.messages.commands.RegisterCommand;
import messaging.messages.requests.PlayerNumberRequest;
import messaging.messages.responses.PlayerNumberResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageConverter;
import seabattleserverrest.ISeaBattleServerRest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements AcceptingSocket, MessageHandlingSocket, WritingSocket {
    private final AsynchronousServerSocketChannel server;
    private final AsynchronousChannelGroup group;
    private final List<AsyncIdentifiableClientSocket> clients = new CopyOnWriteArrayList<>();
    private final ISeaBattleServerRest rest;

    public Server(int port, ISeaBattleServerRest rest) throws IOException {
        this.rest = rest;
        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(100));
        server = AsynchronousServerSocketChannel.open(group);
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 1024);
        server.bind(new InetSocketAddress(port), 100);
        System.out.println("Started server socket on: " + server.getLocalAddress().toString());
        startAccepting();
    }

    public void await() throws InterruptedException {
        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    @Override
    public void startReading(AsyncIdentifiableClientSocket client) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        client.getChannel().read(byteBuffer, byteBuffer, new AsyncReadForHandlingMessageHandler(this, client));
    }

    @Override
    public void startAccepting() {
        server.accept(this, new AsyncAcceptHandler());
    }

    @Override
    public void registerClient(AsyncIdentifiableClientSocket client) {
        clients.add(client);
    }

    @Override
    public void startHandlingMessage(AsyncIdentifiableClientSocket client, byte[] bytes) {
        Object object;
        try {
            object = MessageConverter.convertFromBytes(bytes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (object instanceof PlayerNumberRequest)
        {
            PlayerNumberRequest request = (PlayerNumberRequest) object;
            PlayerNumberResponse response = new PlayerNumberResponse(null, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, null);
            for (var player : clients) {
                if (player.getName() != null)
                    if (player.getName().equals(request.playerName)) {
                        response = new PlayerNumberResponse(player.getNumber(), true);
                        requestMessageHandler = new AsyncRequestMessageHandler(this, client);
                        break;
                    }
            }
            requestMessageHandler.completed(response, request);
        }
        else if (object instanceof RegisterCommand)
        {
            RegisterCommand command = (RegisterCommand) object;
            if (rest.register(command.playername, command.password)) {
                client.setName(command.playername);
                new AsyncCommandMessageHandler(client).completed(null, command);
            } else {
                new AsyncCommandMessageHandler(client).failed(null, command);
            }
        }
        else
            try {
                throw new Exception("Message type not found!");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void startWriting(AsyncIdentifiableClientSocket client, Message message) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            byteBuffer.put(MessageConverter.convertToBytes(message));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("startWriting failed due to message conversion!");
            return;
        }
        byteBuffer.flip();
        client.getChannel().write(byteBuffer, byteBuffer, new AsyncWriteBufferHandler(client));
    }
}
