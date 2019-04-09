package interfaces;

import domain.Player;
import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;

public interface ISeaBattleServerRest {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    int getPlayerNumber(String username);
    Player getPlayer(String username);
}
