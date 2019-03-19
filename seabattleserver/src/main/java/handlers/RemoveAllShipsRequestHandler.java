package handlers;

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

    public RemoveAllShipsRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(RemoveAllShipsRequest request, AsyncIdentifiableClientSocket client) {
        RemoveAllShipsResponse response = new RemoveAllShipsResponse(request.playerNumber, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        // TODO: create remove all ships method
        System.out.println("RemoveAllShipsRequest is not implemented yet!");
    }
}