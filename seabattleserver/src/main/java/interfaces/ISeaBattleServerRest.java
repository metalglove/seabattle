package interfaces;

import domain.Player;
import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;

public interface ISeaBattleServerRest {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    int getPlayerNumber(String username);
    Player getPlayer(String username);

}
