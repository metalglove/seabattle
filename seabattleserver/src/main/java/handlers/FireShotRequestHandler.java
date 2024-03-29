package handlers;

import common.MessageLogger;
import domain.Point;
import domain.ShotType;
import dtos.FireShotResultDto;
import interfaces.ISeaBattleGameAI;
import interfaces.ISeaBattleGameService;
import interfaces.RequestHandler;
import messaging.handlers.AsyncRequestMessageHandler;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.requests.FireShotRequest;
import messaging.messages.responses.FireShotResponse;
import messaging.messages.responses.OpponentFireShotResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FireShotRequestHandler implements RequestHandler<FireShotRequest> {

    private final ClientAwareWritingSocket serverSocket;
    private final ISeaBattleGameService gameService;
    private final ISeaBattleGameAI seaBattleGameAI;
    private final MessageLogger messageLogger;

    public FireShotRequestHandler(ClientAwareWritingSocket serverSocket, ISeaBattleGameService gameService, ISeaBattleGameAI seaBattleGameAI, MessageLogger messageLogger) {
        this.serverSocket = serverSocket;
        this.gameService = gameService;
        this.seaBattleGameAI = seaBattleGameAI;
        this.messageLogger = messageLogger;
    }

    @Override
    public void handle(FireShotRequest request, AsyncIdentifiableClientSocket client) {
        // TODO: refactor
        FireShotResponse response = new FireShotResponse(request.getFiringPlayerNumber(), null, null, null, false);
        OpponentFireShotResponse aiResponse = null;
        AsyncRequestMessageHandler requestMessageHandler = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
        AsyncRequestMessageHandler requestMessageHandlerAI = null;
        int opponent = 0;
        FireShotResultDto fireShotResultDto = gameService.fireShot(request.getFiringPlayerNumber(), request.getPosX(), request.getPosY());
        if (fireShotResultDto != null) {
            Point point = new Point(request.getPosX(), request.getPosY());
            opponent = fireShotResultDto.getReceivingPlayerNumber();
            if (opponent <= 0) {
                response = new FireShotResponse(request.getFiringPlayerNumber(), fireShotResultDto.getShotType(), point, fireShotResultDto.getShip(),true);
                if (fireShotResultDto.getShotType() != ShotType.ALLSUNK) {
                    requestMessageHandlerAI = new AsyncRequestMessageHandler(serverSocket, client, messageLogger);
                    aiResponse = seaBattleGameAI.counterShoot(new OpponentFireShotResponse(fireShotResultDto.getFiringPlayerNumber(), fireShotResultDto.getPoint(), fireShotResultDto.getShotType(), fireShotResultDto.getShip(), true), opponent);
                }
            } else {
                AsyncIdentifiableClientSocket opponentClient = serverSocket.getClientById(opponent);
                if (opponentClient != null) {
                    serverSocket.startWriting(opponentClient, new OpponentFireShotResponse(
                            fireShotResultDto.getFiringPlayerNumber(), fireShotResultDto.getPoint(), fireShotResultDto.getShotType(), fireShotResultDto.getShip(), true));
                    response = new FireShotResponse(request.getFiringPlayerNumber(), fireShotResultDto.getShotType(), point, fireShotResultDto.getShip(),true);
                } else {
                    // Server faulted
                    response = new FireShotResponse(request.getFiringPlayerNumber(), null, point,null, false);
                }
            }
        }
        try {
            requestMessageHandler.completed(response, request);
        } finally {
            // TODO: hopefully it is not to fast :phapoPOGGERS:
            if (requestMessageHandlerAI != null) {
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                final AsyncRequestMessageHandler finalRequestMessageHandlerAI = requestMessageHandlerAI;
                final OpponentFireShotResponse finalAiResponse = aiResponse;
                final int finalOpponent = opponent;
                final FireShotResponse finalResponse = response;
                Runnable task = () -> finalRequestMessageHandlerAI.completed(finalAiResponse, new FireShotRequest(finalOpponent, finalResponse.getPoint().getX(), finalResponse.getPoint().getY()));
                executor.schedule(task, 500, TimeUnit.MILLISECONDS);
                executor.shutdown();
                //requestMessageHandlerAI.completed(aiResponse, new FireShotRequest(opponent, response.point.getX(), response.point.getY()));
            }
        }
    }
}
