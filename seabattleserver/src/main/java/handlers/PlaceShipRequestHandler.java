package handlers;

import domain.Ship;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.PlaceShipRequest;
import messaging.messages.responses.PlaceShipResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class PlaceShipRequestHandler implements RequestHandler<PlaceShipRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public PlaceShipRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(PlaceShipRequest request, AsyncIdentifiableClientSocket client) {
        PlaceShipResponse response = new PlaceShipResponse(request.playerNumber, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        final Ship placedShip = gameService.placeShip(request.playerNumber, request.shipType, request.bowX, request.bowY, request.horizontal);
        if (placedShip != null)
            response = new PlaceShipResponse(request.playerNumber, placedShip, true);
        requestMessageHandler.completed(response, request);
    }
}