package handlers;

import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.StartNewGameRequest;
import messaging.messages.responses.StartNewGameResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class StartNewGameRequestHandler implements RequestHandler<StartNewGameRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public StartNewGameRequestHandler(WritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
    }

    @Override
    public void handle(StartNewGameRequest request, AsyncIdentifiableClientSocket client) {
        StartNewGameResponse response = new StartNewGameResponse(request.playerNumber, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        // TODO: delete game if other player has not yet and register player to a new game
        System.out.println("StartNewGameRequest is not implemented yet!");

    }
}