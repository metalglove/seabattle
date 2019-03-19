package handlers;

import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.RegisterResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class RegisterRequestHandler implements RequestHandler<RegisterRequest> {

    private final WritingSocket serverSocket;
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;

    public RegisterRequestHandler(WritingSocket serverSocket, ISeaBattleServerRest rest, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.rest = rest;
        this.gameService = gameService;
    }

    @Override
    public void handle(RegisterRequest request, AsyncIdentifiableClientSocket client) {
        RegisterResponse response = new RegisterResponse(null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        if (rest.register(request.playerName, request.password)) {
            client.setName(request.playerName);
            int playerNumber = rest.getPlayerNumber(request.playerName);
            client.setNumber(playerNumber);
            gameService.registerPlayer(rest.getPlayer(request.playerName));
            response = new RegisterResponse(playerNumber, true);
        }
        requestMessageHandler.completed(response, request);
    }
}
