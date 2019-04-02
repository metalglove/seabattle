package services;

import dtos.FireShotResultDto;
import interfaces.ISeaBattleGameAI;
import interfaces.ISeaBattleGameService;
import messaging.messages.responses.OpponentFireShotResponse;

public class SeaBattleGameAI implements ISeaBattleGameAI {

    private final ISeaBattleGameService gameService;

    public SeaBattleGameAI(ISeaBattleGameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public OpponentFireShotResponse counterShoot(OpponentFireShotResponse opponentFireShotResponse, Integer cpuID) {
        // TODO: state machine?
        FireShotResultDto fireShotResultDto = gameService.fireShot(cpuID, opponentFireShotResponse.point.getX(), opponentFireShotResponse.point.getY());
        return new OpponentFireShotResponse(cpuID, fireShotResultDto.getPoint(), fireShotResultDto.getShotType(), fireShotResultDto.getShip(), fireShotResultDto.isSuccess());
    }
}