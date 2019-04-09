package restserver.interfaces;

import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;

public interface IUserService {
  LoginResponseDto login(LoginRequestDto loginRequestDto);

  RegisterResponseDto register(RegisterRequestDto registerRequestDto);
}
