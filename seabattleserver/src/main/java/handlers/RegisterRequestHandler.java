package handlers;

import domain.Player;
import dtos.RegisterPlayerResultDto;
import interfaces.ClientAwareWritingSocket;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.RegisterResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class RegisterRequestHandler implements RequestHandler<RegisterRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;

    public RegisterRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleServerRest rest, ISeaBattleGameService gameService) {
        this.serverSocket = serverSocket;
        this.rest = rest;
        this.gameService = gameService;
    }

    @Override
    public void handle(RegisterRequest request, AsyncIdentifiableClientSocket client) {
        RegisterResponse response = new RegisterResponse(null, false, null, null);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
        if (rest.register(request.playerName, request.password)) {
            client.setName(request.playerName);
            int playerNumber = rest.getPlayerNumber(request.playerName);
            client.setNumber(playerNumber);
            serverSocket.registerClient(client);
            Player player = rest.getPlayer(request.playerName);
            RegisterPlayerResultDto registerPlayerResultDto = gameService.registerPlayer(player);
            if (registerPlayerResultDto.getOpponentName() != null) {
                AsyncIdentifiableClientSocket opponent = serverSocket.getClientById(registerPlayerResultDto.getOpponentPlayerNumber());
                serverSocket.startWriting(opponent, new OpponentRegisterResponse(player.getUsername(), player.getPlayerNumber(), true));
            }
            response = new RegisterResponse(playerNumber, registerPlayerResultDto.isSuccess(), registerPlayerResultDto.getOpponentPlayerNumber(), registerPlayerResultDto.getOpponentName());
        }
        requestMessageHandler.completed(response, request);
    }
}
