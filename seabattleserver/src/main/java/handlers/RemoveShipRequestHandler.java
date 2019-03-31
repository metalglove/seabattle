package handlers;

import dtos.RemoveShipResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.RemoveShipRequest;
import messaging.messages.responses.RemoveShipResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageLogger;

public class RemoveShipRequestHandler implements RequestHandler<RemoveShipRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;

    public RemoveShipRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(RemoveShipRequest request, AsyncIdentifiableClientSocket client) {
        RemoveShipResponse response = new RemoveShipResponse(request.playerNumber, null, false);
        RemoveShipResultDto removeShipResultDto = gameService.removeShip(request.playerNumber, request.posX, request.posY);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);

        if(removeShipResultDto.isSuccess())
        response = new RemoveShipResponse(request.playerNumber, removeShipResultDto.getShipToRemove() ,true);

        requestMessageHandler.completed(response, request);
    }
}