package seabattleserverrest;

import domain.Player;

public interface ISeaBattleServerRest {
    boolean register(String username, String password);
    int getPlayerNumber(String username);
    Player getPlayer(String username);
}
