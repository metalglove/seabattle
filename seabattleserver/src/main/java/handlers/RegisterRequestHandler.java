package handlers;

import domain.Player;
import dtos.RegisterPlayerResultDto;
import interfaces.*;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.RegisterResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;
import services.SeaBattleGameAI;

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
        RegisterPlayerResultDto registerPlayerResultDto;
        if (rest.register(request.playerName, request.password)) {
            client.setName(request.playerName);
            int playerNumber = rest.getPlayerNumber(request.playerName);
            client.setNumber(playerNumber);
            serverSocket.registerClient(client);
            Player player = rest.getPlayer(request.playerName);
            if (!request.multiPlayer) {
                registerPlayerResultDto = gameService.registerPlayer(player, false);
            } else {
                registerPlayerResultDto = gameService.registerPlayer(player, true);
                if (registerPlayerResultDto.getOpponentName() != null) {
                    AsyncIdentifiableClientSocket opponent = serverSocket.getClientById(registerPlayerResultDto.getOpponentPlayerNumber());
                    serverSocket.startWriting(opponent, new OpponentRegisterResponse(player.getUsername(), player.getPlayerNumber(), true));
                }
            }

            response = new RegisterResponse(playerNumber, registerPlayerResultDto.isSuccess(), registerPlayerResultDto.getOpponentPlayerNumber(), registerPlayerResultDto.getOpponentName());
        }
        requestMessageHandler.completed(response, request);
    }
}
