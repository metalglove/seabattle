package handlers;

import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.NotifyWhenReadyRequest;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class NotifyWhenReadyRequestHandler implements RequestHandler<NotifyWhenReadyRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public NotifyWhenReadyRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.gameService = gameService;
        this.serverSocket = serverSocket;
    }

    @Override
    public void handle(NotifyWhenReadyRequest request, AsyncIdentifiableClientSocket client) {
        NotifyWhenReadyResponse response = new NotifyWhenReadyResponse(request.playerNumber, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        // TODO: create notification event
        System.out.println("NotifyWhenReadyRequest is not implemented yet!");
    }
}
