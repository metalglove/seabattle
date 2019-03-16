package seabattleserver;

import domain.Ship;
import messaging.handlers.*;
import messaging.interfaces.AcceptingSocket;
import messaging.interfaces.MessageHandlingSocket;
import messaging.interfaces.WritingSocket;
import messaging.messages.Message;
import messaging.messages.requests.*;
import messaging.messages.responses.*;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageConverter;
import interfaces.ISeaBattleServerRest;
import interfaces.ISeaBattleGameService;

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
    private final ISeaBattleGameService gameService;

    public Server(int port, ISeaBattleServerRest rest, ISeaBattleGameService gameService) throws IOException {
        this.rest = rest;
        this.gameService = gameService;
        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(100));
        server = AsynchronousServerSocketChannel.open(group);
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 1024);
        server.bind(new InetSocketAddress(port), 100);
        System.out.println("Started server socket on: " + server.getLocalAddress().toString());
        startAccepting();
    }

    void await() throws InterruptedException {
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
        Message object;
        try {
            object = MessageConverter.convertFromBytes(bytes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (object instanceof PlaceShipRequest) {
            PlaceShipRequest request = (PlaceShipRequest) object;
            PlaceShipResponse response = new PlaceShipResponse(request.playerNumber, null, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            final Ship placedShip = gameService.placeShip(request.playerNumber, request.shipType, request.bowX, request.bowY, request.horizontal);
            if (placedShip != null)
                response = new PlaceShipResponse(request.playerNumber, placedShip, true);

            requestMessageHandler.completed(response, request);
        }
        else if(object instanceof RemoveShipRequest) {
            RemoveShipRequest request = (RemoveShipRequest) object;
            RemoveShipResponse response = new RemoveShipResponse(request.playerNumber, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            // TODO: create remove ship method
        }
        else if(object instanceof RemoveAllShipsRequest) {
            RemoveAllShipsRequest request = (RemoveAllShipsRequest) object;
            RemoveAllShipsResponse response = new RemoveAllShipsResponse(request.playerNumber, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            // TODO: create remove all ships method
        }
        else if(object instanceof NotifyWhenReadyRequest) {
            NotifyWhenReadyRequest request = (NotifyWhenReadyRequest) object;
            NotifyWhenReadyResponse response = new NotifyWhenReadyResponse(request.playerNumber, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            // TODO: create notification event
        }
        else if(object instanceof FireShotRequest) {
            FireShotRequest request = (FireShotRequest) object;
            FireShotResponse response = new FireShotResponse(request.firingPlayerNumber, null, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            // TODO: create fireShot method (that also notifies other player)
        }
        else if(object instanceof StartNewGameRequest) {
            StartNewGameRequest request = (StartNewGameRequest) object;
            StartNewGameResponse response = new StartNewGameResponse(request.playerNumber, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            // TODO: delete game if other player has not yet and register player to a new game
        }
        else if (object instanceof RegisterRequest)
        {
            RegisterRequest request = (RegisterRequest) object;
            RegisterResponse response = new RegisterResponse(null, false);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            if (rest.register(request.playerName, request.password)) {
                client.setName(request.playerName);
                int playerNumber = rest.getPlayerNumber(request.playerName);
                client.setNumber(playerNumber);
                gameService.registerPlayer(rest.getPlayer(request.playerName));
                response = new RegisterResponse(playerNumber, true);
            }
            requestMessageHandler.completed(response, request);
        }
        else if (object instanceof PlaceShipsAutomaticallyRequest)
        {
            PlaceShipsAutomaticallyRequest request = (PlaceShipsAutomaticallyRequest) object;
            final List<Ship> ships = gameService.placeShipsAutomatically(request.playerNumber);
            PlaceShipsAutomaticallyResponse response;
            if (ships == null)
                response = new PlaceShipsAutomaticallyResponse(request.playerNumber, null, false);
            else
                response = new PlaceShipsAutomaticallyResponse(request.playerNumber, ships, true);
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(this, client);
            requestMessageHandler.completed(response, request);
        }
        else {
            try {
                throw new Exception("Message type not found!");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
