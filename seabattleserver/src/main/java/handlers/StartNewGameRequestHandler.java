package handlers;

import common.MessageLogger;
import domain.Player;
import dtos.RegisterPlayerResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.requests.StartNewGameRequest;
import messaging.messages.responses.OpponentRegisterResponse;
import messaging.messages.responses.StartNewGameResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class StartNewGameRequestHandler implements RequestHandler<StartNewGameRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleGameService gameService;
    private final ISeaBattleServerRest rest;
    private final MessageLogger messageLogger;

    public StartNewGameRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleGameService gameService, ISeaBattleServerRest rest, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.rest = rest;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(StartNewGameRequest request, AsyncIdentifiableClientSocket client) {
        StartNewGameResponse response = new StartNewGameResponse(request.getPlayerNumber(), false, null, null);
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        Player player = rest.getPlayer(client.getName());
        if (player != null) {
            RegisterPlayerResultDto registerPlayerResultDto = gameService.registerPlayer(player, request.isMultiPlayer());
            if (registerPlayerResultDto.isSuccess()) {
                response = new StartNewGameResponse(request.getPlayerNumber(), true, registerPlayerResultDto.getOpponentName(), registerPlayerResultDto.getOpponentPlayerNumber());
                if (registerPlayerResultDto.getOpponentName() != null && registerPlayerResultDto.getOpponentPlayerNumber() > 0) {
                    AsyncIdentifiableClientSocket opponent = serverSocket.getClientById(registerPlayerResultDto.getOpponentPlayerNumber());
                    serverSocket.startWriting(opponent, new OpponentRegisterResponse(player.getUsername(), player.getPlayerNumber(), true));
                }
            }
        }

        requestMessageHandler.completed(response, request);
    }
}