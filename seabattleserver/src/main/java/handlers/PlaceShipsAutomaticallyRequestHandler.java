package handlers;

import domain.Ship;
import dtos.PlaceShipsAutomaticallyResultDto;
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
        PlaceShipsAutomaticallyResponse response = new PlaceShipsAutomaticallyResponse(request.playerNumber, null, null,false);;
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        final PlaceShipsAutomaticallyResultDto resultDto = gameService.placeShipsAutomatically(request.playerNumber);

        if (resultDto.isSuccess())
            response = new PlaceShipsAutomaticallyResponse(request.playerNumber, resultDto.getShipsToAdd(), resultDto.getShipsToRemove(), true);

        requestMessageHandler.completed(response, request);
    }
}