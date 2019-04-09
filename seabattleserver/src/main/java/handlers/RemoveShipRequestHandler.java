package handlers;

import common.MessageLogger;
import dtos.RemoveShipResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.RemoveShipRequest;
import messaging.messages.responses.RemoveShipResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

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
    RemoveShipResponse response = new RemoveShipResponse(request.getPlayerNumber(), null, false);
    RemoveShipResultDto removeShipResultDto = gameService.removeShip(request.getPlayerNumber(), request.getPosX(), request.getPosY());
    AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);

    if (removeShipResultDto.isSuccess())
      response = new RemoveShipResponse(request.getPlayerNumber(), removeShipResultDto.getShipToRemove(), true);

    requestMessageHandler.completed(response, request);
  }
}