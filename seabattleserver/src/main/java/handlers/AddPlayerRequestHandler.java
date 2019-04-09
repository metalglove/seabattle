package handlers;

import common.MessageLogger;
import domain.Player;
import dtos.AddPlayerResultDto;
import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.requests.AddPlayerRequest;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.AddPlayerResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class AddPlayerRequestHandler implements RequestHandler<AddPlayerRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleServerRest rest;
    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;

    public AddPlayerRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleServerRest rest, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.rest = rest;
        this.gameService = gameService;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(AddPlayerRequest request, AsyncIdentifiableClientSocket client) {
        AddPlayerResponse response = new AddPlayerResponse(null, false, null, null, false);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        AddPlayerResultDto addPlayerResultDto;
        RegisterResponseDto registerResponseDto = new RegisterResponseDto(null,null,false,null);

        LoginResponseDto loginResponseDto = rest.login(new LoginRequestDto(request.getPlayerName(), request.getPassword().toCharArray()));
        // Try logging in first
        if(!loginResponseDto.getSuccess()){
            registerResponseDto = rest.register(new RegisterRequestDto(request.getPlayerName(), request.getPassword().toCharArray()));
        }

        if (registerResponseDto.getSuccess() || loginResponseDto.getSuccess()) {
            client.setName(request.getPlayerName());
            int playerNumber = rest.getPlayerNumber(request.getPlayerName());
            client.setNumber(playerNumber);
            serverSocket.registerClient(client);
            Player player = rest.getPlayer(request.getPlayerName());
            if (!request.isMultiPlayer()) {
                addPlayerResultDto = gameService.addPlayer(player, false);
            } else {
                addPlayerResultDto = gameService.addPlayer(player, true);
                if (addPlayerResultDto.getOpponentName() != null) {
                    AsyncIdentifiableClientSocket opponent = serverSocket.getClientById(addPlayerResultDto.getOpponentPlayerNumber());
                    serverSocket.startWriting(opponent, new OpponentRegisterResponse(player.getUsername(), player.getPlayerNumber(), true));
                }
            }
            response = new AddPlayerResponse(playerNumber, addPlayerResultDto.isSuccess(), addPlayerResultDto.getOpponentPlayerNumber(), addPlayerResultDto.getOpponentName(), false);
        }
        else if (registerResponseDto.getMessage().equals("The user already exists.")) {
            response = new AddPlayerResponse(null, false, null, null, true);
        }
        requestMessageHandler.completed(response, request);
    }
}