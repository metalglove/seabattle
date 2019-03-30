package handlers;

import domain.Point;
import domain.ShotType;
import dtos.FireShotResultDto;
import interfaces.ClientAwareWritingSocket;
import interfaces.ISeaBattleGameAI;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.messages.requests.FireShotRequest;
import messaging.messages.responses.FireShotResponse;
import messaging.messages.responses.OpponentFireShotResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;
import services.SeaBattleGameAI;

public class FireShotRequestHandler implements RequestHandler<FireShotRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleGameService gameService;
    private final ISeaBattleGameAI seaBattleGameAI;

    public FireShotRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleGameService gameService, ISeaBattleGameAI seaBattleGameAI) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.seaBattleGameAI = seaBattleGameAI;
    }

    @Override
    public void handle(FireShotRequest request, AsyncIdentifiableClientSocket client) {
        // TODO: refactor
        FireShotResponse response = new FireShotResponse(request.firingPlayerNumber, null, null, null, false);
        OpponentFireShotResponse aiResponse = null;
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        AsyncRequestMessageHandler requestMessageHandlerAI = null;
        int opponent = 0; // TODO: zds
        FireShotResultDto fireShotResultDto = gameService.fireShot(request.firingPlayerNumber, request.posX, request.posY);
        if (fireShotResultDto != null) {
            Point point = new Point(request.posX, request.posY);
            opponent = fireShotResultDto.getReceivingPlayerNumber();
            if (opponent <= 0) {
                response = new FireShotResponse(request.firingPlayerNumber, fireShotResultDto.getShotType(), point, fireShotResultDto.getShip(),true);
                if (fireShotResultDto.getShotType() != ShotType.ALLSUNK) {
                    requestMessageHandlerAI = new AsyncRequestMessageHandler(serverSocket, client);
                    aiResponse = seaBattleGameAI.counterShoot(new OpponentFireShotResponse(fireShotResultDto.getFiringPlayerNumber(), fireShotResultDto.getPoint(), fireShotResultDto.getShotType(), fireShotResultDto.getShip(), true), opponent);
                }
            } else {
                AsyncIdentifiableClientSocket opponentClient = serverSocket.getClientById(opponent);
                if (opponentClient != null) {
                    serverSocket.startWriting(opponentClient, new OpponentFireShotResponse(
                            fireShotResultDto.getFiringPlayerNumber(), fireShotResultDto.getPoint(), fireShotResultDto.getShotType(), fireShotResultDto.getShip(), true));
                    response = new FireShotResponse(request.firingPlayerNumber, fireShotResultDto.getShotType(), point, fireShotResultDto.getShip(),true);
                } else {
                    // Server faulted
                    response = new FireShotResponse(request.firingPlayerNumber, null, point,null, false);
                }
            }
        }
        try {
            requestMessageHandler.completed(response, request);

        } finally {
            // TODO: hopefully it is not to fast :phapoPOGGERS:
            if (requestMessageHandlerAI != null) {
                requestMessageHandlerAI.completed(aiResponse, new FireShotRequest(opponent, response.point.getX(), response.point.getY()));
            }
        }
    }
}
