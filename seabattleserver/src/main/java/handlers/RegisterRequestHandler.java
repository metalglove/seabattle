package handlers;

import common.MessageLogger;
import domain.Player;
import dtos.RegisterPlayerResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.RegisterResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class RegisterRequestHandler implements RequestHandler<RegisterRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;

    public RegisterRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleServerRest rest, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.rest = rest;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(RegisterRequest request, AsyncIdentifiableClientSocket client) {
        RegisterResponse response = new RegisterResponse(null, false, null, null);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        RegisterPlayerResultDto registerPlayerResultDto;
        if (rest.register(request.getPlayerName(), request.getPassword())) {
            client.setName(request.getPlayerName());
            int playerNumber = rest.getPlayerNumber(request.getPlayerName());
            client.setNumber(playerNumber);
            serverSocket.registerClient(client);
            Player player = rest.getPlayer(request.getPlayerName());
            if (!request.isMultiPlayer()) {
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
