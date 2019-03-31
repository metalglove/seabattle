package seabattleserver;

import handlers.*;
import interfaces.IFactory;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncAcceptHandler;
import messaging.handlers.AsyncCrashHandler;
import messaging.handlers.AsyncReadForHandlingMessageHandler;
import messaging.handlers.AsyncWriteBufferHandler;
import messaging.interfaces.AcceptingSocket;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.interfaces.MessageHandlingSocket;
import messaging.messages.Message;
import messaging.messages.requests.*;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageConverter;
import messaging.utilities.MessageLogger;
import services.SeaBattleGameAI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements AcceptingSocket, MessageHandlingSocket, ClientAwareWritingSocket {
    private final AsynchronousServerSocketChannel server;
    private final AsynchronousChannelGroup group;
    // TODO: implement mapping for 2 player to game, for improved handling of requests where a player id is used
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;
    private final ConcurrentMap<Integer, AsyncIdentifiableClientSocket> clientMapping = new ConcurrentHashMap<>();
    private final ConcurrentMap<Class<?>, IFactory<RequestHandler>> requestHandlerMapping = new ConcurrentHashMap<>();
    private final MessageLogger serverMessageLogger;
    private final MessageLogger handlerMessageLogger;
    private final MessageLogger requestMessageLogger;

    public Server(int port, ISeaBattleServerRest rest, ISeaBattleGameService gameService, MessageLogger messageLogger) throws IOException {
        this.rest = rest;
        this.gameService = gameService;
        this.serverMessageLogger = messageLogger;
        this.handlerMessageLogger = new MessageLogger("SOCKET-HANDLER");
        this.requestMessageLogger = new MessageLogger("REQUEST-HANDLER");
        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(100));
        server = AsynchronousServerSocketChannel.open(group);
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 2048);
        server.bind(new InetSocketAddress(port), 100);
        messageLogger.info("Started server socket on: " + server.getLocalAddress().toString());
        startAccepting();

        requestHandlerMapping.put(RegisterRequest.class, () -> new RegisterRequestHandler(this, rest, gameService, requestMessageLogger));
        requestHandlerMapping.put(FireShotRequest.class, () -> new FireShotRequestHandler(this, gameService, new SeaBattleGameAI(gameService), requestMessageLogger));
        requestHandlerMapping.put(NotifyWhenReadyRequest.class, () -> new NotifyWhenReadyRequestHandler(this, gameService, requestMessageLogger));
        requestHandlerMapping.put(PlaceShipRequest.class, () -> new PlaceShipRequestHandler(this, gameService, requestMessageLogger));
        requestHandlerMapping.put(PlaceShipsAutomaticallyRequest.class, () -> new PlaceShipsAutomaticallyRequestHandler(this, gameService, requestMessageLogger));
        requestHandlerMapping.put(RemoveAllShipsRequest.class, () -> new RemoveAllShipsRequestHandler(this, gameService, requestMessageLogger));
        requestHandlerMapping.put(RemoveShipRequest.class, () -> new RemoveShipRequestHandler(this, gameService, requestMessageLogger));
        requestHandlerMapping.put(StartNewGameRequest.class, () -> new StartNewGameRequestHandler(this, gameService, rest, requestMessageLogger));
    }

    void await() throws InterruptedException {
        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    @Override
    public void startReading(AsyncIdentifiableClientSocket client) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        client.getChannel().read(byteBuffer, byteBuffer, new AsyncReadForHandlingMessageHandler(this, client, new AsyncCrashHandler(this, client, handlerMessageLogger), handlerMessageLogger));
    }

    @Override
    public void startAccepting() {
        server.accept(this, new AsyncAcceptHandler(handlerMessageLogger));
    }

    @Override
    public void registerClient(AsyncIdentifiableClientSocket client) {
        clientMapping.put(client.getNumber(), client);
    }

    @Override
    public void unRegisterClient(AsyncIdentifiableClientSocket client) {
        AsyncIdentifiableClientSocket presentClient = clientMapping.computeIfPresent(client.getNumber(), new GameCrashHandler(gameService, this, handlerMessageLogger));
        if (presentClient != null) {
            clientMapping.remove(client.getNumber());
            serverMessageLogger.info("Successfully removed client from client list.");
        } else {
            serverMessageLogger.error("Failed to remove client from client list.");
        }
    }

    @Override
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
        serverMessageLogger.info("Message is " + object.getClass().getSimpleName());
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
            serverMessageLogger.error("startWriting failed due to message conversion!");
            return;
        }
        byteBuffer.flip();
        client.getChannel().write(byteBuffer, byteBuffer, new AsyncWriteBufferHandler(client, handlerMessageLogger));
    }
}
