package interfaces;

import messaging.messages.responses.OpponentFireShotResponse;

public interface ISeaBattleGameAI {
    OpponentFireShotResponse counterShoot(OpponentFireShotResponse opponentFireShotResponse, Integer cpuID);
}
