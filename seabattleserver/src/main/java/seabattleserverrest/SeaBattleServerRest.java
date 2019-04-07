package seabattleserverrest;

import common.MessageLogger;
import domain.Player;
import interfaces.ISeaBattleServerRest;
import rest.SeaBattleResponse;
import rest.SeaBattleRestService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SeaBattleServerRest implements ISeaBattleServerRest {
    private Map<Integer, Player> players = new HashMap<>();
    private Integer counter = 0;
    private final String url = "/seabattle";
    private SeaBattleRestService seaBattleRestService;
    //private final MessageLogger messageLogger;
    //TODO: Finish REST implementation
    @Override
    public boolean register(String username, String password) {
//        if(seaBattleRestService.getUserNameInUse(username))
//            SeaBattleResponse response =
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

    private SeaBattleResponse executeHttpUriRequest(HttpUriRequest httpUriRequest) {

        // Execute the HttpUriRequest
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
            //logger.info("[Status Line] : " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            //log.info("[Entity] : " + entityString);
            Gson gson = new Gson();
            SeaBattleResponse seaBattleResponse = gson.fromJson(entityString, SeaBattleResponse.class);
            return seaBattleResponse;
        } catch (IOException e) {
            // log.info("IOException : " + e.toString());
            SeaBattleResponse seaBattleResponse = new SeaBattleResponse();
            seaBattleResponse.setSuccess(false);
            return seaBattleResponse;
        } catch (JsonSyntaxException e) {
            // log.info("JsonSyntaxException : " + e.toString());
            SeaBattleResponse petStoreResponse = new SeaBattleResponse();
            petStoreResponse.setSuccess(false);
            return petStoreResponse;
        }
    }
}
