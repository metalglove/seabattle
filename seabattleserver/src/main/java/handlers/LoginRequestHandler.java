package handlers;

import common.MessageLogger;
import domain.Player;
import dtos.RegisterPlayerResultDto;
import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.requests.RegisterRequest;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.LoginResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class LoginRequestHandler implements RequestHandler<RegisterRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;

    public LoginRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleServerRest rest, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.rest = rest;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(RegisterRequest request, AsyncIdentifiableClientSocket client) {
        LoginResponse response = new LoginResponse(null, false, null, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        RegisterPlayerResultDto registerPlayerResultDto;
        LoginResponseDto loginResponseDto = rest.login(new LoginRequestDto(request.getPlayerName(), request.getPassword().toCharArray()));
        if (loginResponseDto.getSuccess()) {
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
            response = new LoginResponse(playerNumber, registerPlayerResultDto.isSuccess(), registerPlayerResultDto.getOpponentPlayerNumber(), registerPlayerResultDto.getOpponentName(), true);
        }
        else{ //TODO: Check for credentials correct vs name doesnt exist.
            response = new LoginResponse(null, false, null, null, false);
        }
        requestMessageHandler.completed(response, request);
    }
}
