package handlers;

import domain.Ship;
import dtos.RemoveAllShipsResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.RemoveAllShipsRequest;
import messaging.messages.responses.PlaceShipsAutomaticallyResponse;
import messaging.messages.responses.RemoveAllShipsResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.util.List;

public class RemoveAllShipsRequestHandler implements RequestHandler<RemoveAllShipsRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public RemoveAllShipsRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(RemoveAllShipsRequest request, AsyncIdentifiableClientSocket client) {
        RemoveAllShipsResponse response = new RemoveAllShipsResponse(request.playerNumber, null, false);
        RemoveAllShipsResultDto removeAllShipsResultDto = gameService.removeAllShips(request.playerNumber);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        if(removeAllShipsResultDto.isSuccess())
            response = new RemoveAllShipsResponse(request.playerNumber, removeAllShipsResultDto.getShipsToRemove() ,true);

        requestMessageHandler.completed(response, request);
    }
}