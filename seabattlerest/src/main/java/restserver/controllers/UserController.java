package restserver.controllers;

import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import restserver.interfaces.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

  private final IUserService userService;

  @Autowired
  public UserController(@Qualifier("userService") IUserService userService) {
    this.userService = userService;
  }

  @PutMapping(value = "/login", consumes = "application/x-yaml", produces = "application/x-yaml")
  public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
    return userService.login(loginRequestDto);
  }

  @PostMapping(value = "/register", consumes = "application/x-yaml", produces = "application/x-yaml")
  public RegisterResponseDto register(@RequestBody RegisterRequestDto registerRequestDto) {
    return userService.register(registerRequestDto);
  }
}
