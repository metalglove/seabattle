package handlers;

import common.MessageLogger;
import dtos.RemoveAllShipsResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.RemoveAllShipsRequest;
import messaging.messages.responses.RemoveAllShipsResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class RemoveAllShipsRequestHandler implements RequestHandler<RemoveAllShipsRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;

    public RemoveAllShipsRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(RemoveAllShipsRequest request, AsyncIdentifiableClientSocket client) {
        RemoveAllShipsResponse response = new RemoveAllShipsResponse(request.getPlayerNumber(), null, false);
        RemoveAllShipsResultDto removeAllShipsResultDto = gameService.removeAllShips(request.getPlayerNumber());
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        if(removeAllShipsResultDto.isSuccess())
            response = new RemoveAllShipsResponse(request.getPlayerNumber(), removeAllShipsResultDto.getShipsToRemove() ,true);

        requestMessageHandler.completed(response, request);
    }
}