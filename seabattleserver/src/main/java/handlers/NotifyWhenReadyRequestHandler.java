package handlers;

import common.MessageLogger;
import dtos.SetReadyResultDto;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.requests.NotifyWhenReadyRequest;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.util.Random;

public class NotifyWhenReadyRequestHandler implements RequestHandler<NotifyWhenReadyRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final MessageLogger messageLogger;
    private final ISeaBattleGameService gameService;
    private static final Random rand = new Random();

    public NotifyWhenReadyRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleGameService gameService, MessageLogger messageLogger) {
        this.gameService = gameService;
        this.serverSocket = serverSocket;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(NotifyWhenReadyRequest request, AsyncIdentifiableClientSocket client) {
        SetReadyResultDto setReadyResultDto = gameService.setReady(request.getPlayerNumber());
        messageLogger.info("Player : " + request.getPlayerNumber() + " wants to be notified when game can start.");

        if (!setReadyResultDto.isSuccess()) {
            messageLogger.info("Failed to notify player");
            serverSocket.startWriting(client, new NotifyWhenReadyResponse(null,false,false));
        } else if (setReadyResultDto.isBothReady()) {
            boolean isPlayersTurn = rand.nextBoolean();
            if (setReadyResultDto.getOpponentPlayerNumber() <= 0)
                isPlayersTurn = true;
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
            NotifyWhenReadyResponse response = new NotifyWhenReadyResponse(request.getPlayerNumber(), true, isPlayersTurn);
            int opponentPlayerNumber = setReadyResultDto.getOpponentPlayerNumber();
            if (opponentPlayerNumber > 0) {
                AsyncIdentifiableClientSocket opponent = serverSocket.getClientById(opponentPlayerNumber);
                serverSocket.startWriting(opponent, new NotifyWhenReadyResponse(opponentPlayerNumber, true, !isPlayersTurn));
            }
            requestMessageHandler.completed(response, request);
            messageLogger.info("Players : " + request.getPlayerNumber() + " & " + opponentPlayerNumber + " are notified.");
        } else {
            messageLogger.info("Both players are not ready yet.");
        }
    }
}
