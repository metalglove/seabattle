package handlers;

import domain.Point;
import dtos.FireShotResultDto;
import interfaces.ClientAwareWritingSocket;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.messages.requests.FireShotRequest;
import messaging.messages.responses.FireShotResponse;
import messaging.messages.responses.OpponentFireShotResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class FireShotRequestHandler implements RequestHandler<FireShotRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public FireShotRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(FireShotRequest request, AsyncIdentifiableClientSocket client) {
        // TODO: refactor
        FireShotResponse response = new FireShotResponse(request.firingPlayerNumber, null, null, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        FireShotResultDto fireShotResultDto = gameService.fireShot(request.firingPlayerNumber, request.posX, request.posY);
        if (fireShotResultDto != null) {
            Point point = new Point(request.posX, request.posY);
            int opponent = fireShotResultDto.getReceivingPlayerNumber();
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
        requestMessageHandler.completed(response, request);
    }
}
