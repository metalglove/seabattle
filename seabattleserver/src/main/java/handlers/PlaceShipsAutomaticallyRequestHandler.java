package handlers;

import common.MessageLogger;
import dtos.PlaceShipsAutomaticallyResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.PlaceShipsAutomaticallyRequest;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class PlaceShipsAutomaticallyRequestHandler implements RequestHandler<PlaceShipsAutomaticallyRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;

    public PlaceShipsAutomaticallyRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(PlaceShipsAutomaticallyRequest request, AsyncIdentifiableClientSocket client) {
        PlaceShipsAutomaticallyResponse response = new PlaceShipsAutomaticallyResponse(request.getPlayerNumber(), null, null,false);;
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        final PlaceShipsAutomaticallyResultDto resultDto = gameService.placeShipsAutomatically(request.getPlayerNumber());

        if (resultDto.isSuccess())
            response = new PlaceShipsAutomaticallyResponse(request.getPlayerNumber(), resultDto.getShipsToAdd(), resultDto.getShipsToRemove(), true);

        requestMessageHandler.completed(response, request);
    }
}