package seabattleserverrest;

import common.MessageLogger;
import domain.Player;
import dtos.requests.LoginRequestDto;
import dtos.requests.RegisterRequestDto;
import dtos.responses.LoginResponseDto;
import dtos.responses.RegisterResponseDto;
import http.QueryResponse;
import http.ServiceBase;
import interfaces.ISeaBattleServerRest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.util.HashMap;
import java.util.Map;

public class SeaBattleServerRest extends ServiceBase implements ISeaBattleServerRest {
    private Map<Integer, Player> players = new HashMap<>();

    public SeaBattleServerRest(MessageLogger messageLogger) {
        super(messageLogger);
    }


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        final QueryResponse<LoginResponseDto> queryResponse = executeQuery(loginRequestDto, LoginResponseDto.class, "/user/login", new HttpPut());
        if (queryResponse.isSuccess()) {
            LoginResponseDto loginResponseDto = queryResponse.getResponse();

                if (players.containsValue(loginResponseDto.getUsername())){
                    return queryResponse.getResponse();
                }
            players.put(loginResponseDto.getId().intValue(), new Player(loginResponseDto.getUsername(), loginResponseDto.getId().intValue()));
        }else {
            messageLogger.info(String.format("Login failed:  %s", queryResponse.getReasonIfFailed()));
        }
        return queryResponse.getResponse();
    }

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        final QueryResponse<RegisterResponseDto> queryResponse = executeQuery(registerRequestDto, RegisterResponseDto.class, "/user/register", new HttpPost());
        if (queryResponse.isSuccess()) {
            messageLogger.info("Register success");
            RegisterResponseDto registerResponseDto = queryResponse.getResponse();
            players.put(registerResponseDto.getId(), new Player(registerResponseDto.getUsername(), registerResponseDto.getId()));
        } else {
            messageLogger.info(String.format("Register failed: %s", queryResponse.getReasonIfFailed()));
        }
        return queryResponse.getResponse();
    }

    @Override
    public int getPlayerNumber(String username) {
        for (Player player : players.values()) {
            if (player.equals(username)) {
                return player.getPlayerNumber();
            }
        }
        return -1;
    }

    @Override
    public Player getPlayer(String username) {
        for (Player player : players.values()) {
            if (player.equals(username)) {
                return player;
            }
        }
        return null;
    }
}
