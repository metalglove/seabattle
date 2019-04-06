package handlers;

import common.MessageLogger;
import dtos.PlaceShipResultDto;
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
    private final MessageLogger messageLogger;

    public PlaceShipRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(PlaceShipRequest request, AsyncIdentifiableClientSocket client) {
        PlaceShipResponse response = new PlaceShipResponse(request.getPlayerNumber(), null, false, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        final PlaceShipResultDto placeShipResultDto = gameService.placeShip(request.getPlayerNumber(), request.getShipType(), request.getBowX(), request.getBowY(), request.isHorizontal());
        if (placeShipResultDto.getShip() != null)
            response = new PlaceShipResponse(request.getPlayerNumber(), placeShipResultDto.getShip(), true, placeShipResultDto.getOldShip(), placeShipResultDto.getHasPlacedAllShips());
        requestMessageHandler.completed(response, request);
    }
}