package handlers;

import domain.Ship;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.PlaceShipsAutomaticallyRequest;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.util.List;

public class PlaceShipsAutomaticallyRequestHandler implements RequestHandler<PlaceShipsAutomaticallyRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public PlaceShipsAutomaticallyRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(PlaceShipsAutomaticallyRequest request, AsyncIdentifiableClientSocket client) {
        final List<Ship> ships = gameService.placeShipsAutomatically(request.playerNumber);
        PlaceShipsAutomaticallyResponse response;
        if (ships == null)
            response = new PlaceShipsAutomaticallyResponse(request.playerNumber, null, false);
        else
            response = new PlaceShipsAutomaticallyResponse(request.playerNumber, ships, true);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        requestMessageHandler.completed(response, request);
    }
}