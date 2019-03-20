package handlers;

import dtos.SetReadyResultDto;
import interfaces.ClientAwareWritingSocket;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.WritingSocket;
import messaging.messages.requests.NotifyWhenReadyRequest;
import messaging.messages.responses.NotifyWhenReadyResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

public class NotifyWhenReadyRequestHandler implements RequestHandler<NotifyWhenReadyRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleGameService gameService;

    public NotifyWhenReadyRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleGameService gameService) {
        this.gameService = gameService;
        this.serverSocket = serverSocket;
    }

    @Override
    public void handle(NotifyWhenReadyRequest request, AsyncIdentifiableClientSocket client) {
        SetReadyResultDto setReadyResultDto = gameService.setReady(request.playerNumber);
        System.out.println("Player : "+ request.playerNumber+",wants to be notified when game can start.");
        if (setReadyResultDto != null && setReadyResultDto.isBothReady()) {
            AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client);
            NotifyWhenReadyResponse response = new NotifyWhenReadyResponse(request.playerNumber, true);
            int opponentPlayerNumber = setReadyResultDto.getOpponentPlayerNumber();
            AsyncIdentifiableClientSocket opponent = serverSocket.getClientById(opponentPlayerNumber);
            serverSocket.startWriting(opponent, new NotifyWhenReadyResponse(opponentPlayerNumber, true));
            requestMessageHandler.completed(response, request);
            System.out.println("Players : "+ request.playerNumber+" & " + opponentPlayerNumber + ", are notified.");
        }
    }
}
