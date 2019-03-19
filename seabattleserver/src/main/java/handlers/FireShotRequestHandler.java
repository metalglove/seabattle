package handlers;

import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.FireShotRequest;
import messaging.messages.responses.FireShotResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class FireShotRequestHandler implements RequestHandler<FireShotRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public FireShotRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(FireShotRequest request, AsyncIdentifiableClientSocket client) {
        FireShotResponse response = new FireShotResponse(request.firingPlayerNumber, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        // TODO: create fireShot method (that also notifies other player)
        System.out.println("FireShotRequestHandler is not implemented yet!");
    }
}
