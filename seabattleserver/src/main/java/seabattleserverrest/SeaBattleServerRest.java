package seabattleserverrest;

import java.util.HashMap;
import java.util.Map;

public class SeaBattleServerRest implements ISeaBattleServerRest {
    private Map<String, String> players = new HashMap<>();

    @Override
    public boolean register(String username, String password) {
        if (players.containsKey(username))
            return false;
        players.put(username, password);
        return true;
    }
}
