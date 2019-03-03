package seabattleserverrest;

import domain.Player;

import java.util.HashMap;
import java.util.Map;

public class SeaBattleServerRest implements ISeaBattleServerRest {
    private Map<Integer, Player> players = new HashMap<>();
    private Integer counter = 0;

    @Override
    public boolean register(String username, String password) {
        if (players.containsValue(username))
            return false;
        counter++;
        Integer count = counter;
        players.put(counter, new Player(username, password, count));
        return true;
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
