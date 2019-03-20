package handlers;

import dtos.FireShotResultDto;
import interfaces.ClientAwareWritingSocket;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
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
        FireShotResponse response = new FireShotResponse(request.firingPlayerNumber, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        FireShotResultDto fireShotResultDto = gameService.fireShot(request.firingPlayerNumber, request.posX, request.posY);
        if (fireShotResultDto != null) {
            int opponent = fireShotResultDto.getReceivingPlayerNumber();
            AsyncIdentifiableClientSocket opponentClient = serverSocket.getClientById(opponent);
            if (opponentClient != null) {
                serverSocket.startWriting(opponentClient, new OpponentFireShotResponse(
                        fireShotResultDto.getFiringPlayerNumber(), fireShotResultDto.getPoint(), fireShotResultDto.getShotType(), true));
            } else {
                // Server faulted
                response = new FireShotResponse(request.firingPlayerNumber, null, false);
            }
        }
        requestMessageHandler.completed(response, request);
    }
}
