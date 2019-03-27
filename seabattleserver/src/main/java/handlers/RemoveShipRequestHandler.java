package handlers;

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

    public RemoveShipRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(RemoveShipRequest request, AsyncIdentifiableClientSocket client) {
      //  RemoveShipResponse response = new RemoveShipResponse(request.playerNumber, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        // TODO: create remove ship method
        System.out.println("RemoveShipRequest is not implemented yet!");
    }
}