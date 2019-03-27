package seabattleserver;

import domain.Game;
import handlers.*;
import interfaces.*;
import messaging.handlers.AsyncAcceptHandler;
import messaging.handlers.AsyncReadForHandlingMessageHandler;
import messaging.handlers.AsyncWriteBufferHandler;
import messaging.interfaces.AcceptingSocket;
import messaging.interfaces.MessageHandlingSocket;
import messaging.interfaces.WritingSocket;
import messaging.messages.Message;
import messaging.messages.requests.*;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageConverter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.*;

public class Server implements AcceptingSocket, MessageHandlingSocket, ClientAwareWritingSocket {
    private final AsynchronousServerSocketChannel server;
    private final AsynchronousChannelGroup group;
    // TODO: implement mapping for 2 player to game, for improved handling of requests where a player id is used
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;
    private final ConcurrentMap<Integer, AsyncIdentifiableClientSocket> clientMapping = new ConcurrentHashMap<>();
    private final ConcurrentMap<Class<?>, IFactory<RequestHandler>> requestHandlerMapping = new ConcurrentHashMap<>();

    public Server(int port, ISeaBattleServerRest rest, ISeaBattleGameService gameService) throws IOException {
        this.rest = rest;
        this.gameService = gameService;
        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(100));
        server = AsynchronousServerSocketChannel.open(group);
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 2048);
        server.bind(new InetSocketAddress(port), 100);
        System.out.println("Started server socket on: " + server.getLocalAddress().toString());
        startAccepting();

        requestHandlerMapping.put(RegisterRequest.class, () -> new RegisterRequestHandler(this, rest, gameService));
        requestHandlerMapping.put(FireShotRequest.class, () -> new FireShotRequestHandler(this, gameService));
        requestHandlerMapping.put(NotifyWhenReadyRequest.class, () -> new NotifyWhenReadyRequestHandler(this, gameService));
        requestHandlerMapping.put(PlaceShipRequest.class, () -> new PlaceShipRequestHandler(this, gameService));
        requestHandlerMapping.put(PlaceShipsAutomaticallyRequest.class, () -> new PlaceShipsAutomaticallyRequestHandler(this, gameService));
        requestHandlerMapping.put(RemoveAllShipsRequest.class, () -> new RemoveAllShipsRequestHandler(this, gameService));
        requestHandlerMapping.put(RemoveShipRequest.class, () -> new RemoveShipRequestHandler(this, gameService));
        requestHandlerMapping.put(StartNewGameRequest.class, () -> new StartNewGameRequestHandler(this, gameService, rest));
    }

    void await() throws InterruptedException {
        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    @Override
    public void startReading(AsyncIdentifiableClientSocket client) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        client.getChannel().read(byteBuffer, byteBuffer, new AsyncReadForHandlingMessageHandler(this, client));
    }

    @Override
    public void startAccepting() {
        server.accept(this, new AsyncAcceptHandler());
    }

    @Override
    public void registerClient(AsyncIdentifiableClientSocket client) {
        clientMapping.put(client.getNumber(), client);
    }

    public AsyncIdentifiableClientSocket getClientById(int id) {
        return clientMapping.get(id);
    }

    @Override
    public void startHandlingMessage(AsyncIdentifiableClientSocket client, byte[] bytes) {
        Message object;
        try {
            object = MessageConverter.convertFromBytes(bytes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Message is " + object.getClass().getSimpleName());
        IFactory<RequestHandler> requestHandlerFactory = requestHandlerMapping.get(object.getClass());
        RequestHandler handler = requestHandlerFactory.create();
        handler.handle(object, client);
    }

    @Override
    public void startWriting(AsyncIdentifiableClientSocket client, Message message) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
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
