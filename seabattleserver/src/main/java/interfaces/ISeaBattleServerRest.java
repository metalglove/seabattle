package interfaces;

import domain.Player;
import dtos.requests.RegisterRequestDto;
import dtos.responses.RegisterResponseDto;

public interface ISeaBattleServerRest {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    int getPlayerNumber(String username);
    Player getPlayer(String username);
}
