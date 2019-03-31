package handlers;

import dtos.EndgameResultDto;
import interfaces.ISeaBattleGameService;
import messaging.interfaces.ClientAwareWritingSocket;
import messaging.messages.responses.ErrorResponse;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageLogger;

import java.io.IOException;
import java.util.function.BiFunction;

public class GameCrashHandler implements BiFunction<Integer, AsyncIdentifiableClientSocket, AsyncIdentifiableClientSocket> {

    private final ISeaBattleGameService gameService;
    private final MessageLogger messageLogger;
    private final ClientAwareWritingSocket serverSocket;

    public GameCrashHandler(ISeaBattleGameService gameService, ClientAwareWritingSocket clientAwareWritingSocket, MessageLogger messageLogger) {
        this.gameService = gameService;
        this.messageLogger = messageLogger;
        this.serverSocket = clientAwareWritingSocket;
    }

    @Override
    public AsyncIdentifiableClientSocket apply(Integer integer, AsyncIdentifiableClientSocket client) {
        try {
            client.getChannel().close();
        } catch (IOException e) {
            messageLogger.error("Attempting to close channel resulted in: " + e.getMessage());
        }
        EndgameResultDto endgameResultDto = gameService.endGame(client.getNumber());
        if (endgameResultDto.isSuccess()) {
            messageLogger.info("Game ended successfully!");
            if (endgameResultDto.getOpponentPlayerId() != null) {
                if (endgameResultDto.getOpponentPlayerId() > 0) {
                    AsyncIdentifiableClientSocket opponentSocketClient =  serverSocket.getClientById(endgameResultDto.getOpponentPlayerId());
                    if (opponentSocketClient != null) {
                        serverSocket.startWriting(opponentSocketClient, new ErrorResponse("Opponent crashed", opponentSocketClient.getName(), opponentSocketClient.getNumber()));
                    }
                } else {
                    messageLogger.info("SinglePlayer game does not need to notify opponent because the server is the opponent.");
                }
            } else {
                messageLogger.info("Player did not have an opponent yet, thus no need to notify an opponent.");
            }
        } else {
            messageLogger.error("Game failed to end...");
        }
        return client;
    }
}
