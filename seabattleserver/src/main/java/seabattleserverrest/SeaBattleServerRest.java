package seabattleserverrest;

import domain.Player;
import interfaces.ISeaBattleServerRest;

import java.util.HashMap;
import java.util.Map;

public class SeaBattleServerRest implements ISeaBattleServerRest {
    private Map<Integer, Player> players = new HashMap<>();
    private Integer counter = 0;
    //TODO: Add export in seabattlerest.
    // private SeaBattleRESTService seaBattleRESTService;


    //    public boolean getUserInUse(int petId) {
    //        String query (url of RESTService method) = "/getUsername";
    //        SeaBattleResponse response = executeQueryGet(query);
    //        return response.isSuccess();
    //    }
    //
    //    private PetStoreResponse executeQueryGet(String queryGet) {
    //
    //        // Build the query for the REST service
    //        final String query = url + queryGet;
    //        log.info("[Query Get] : " + query);
    //
    //        // Execute the HTTP GET request
    //        HttpGet httpGet = new HttpGet(query);
    //        return executeHttpUriRequest(httpGet);
    //    }
    //  private PetStoreResponse executeQueryPost(PetDTO petRequest, String queryPost) {
    //
    //        // Build the query for the REST service
    //        final String query = url + queryPost;
    //        log.info("[Query Post] : " + query);
    //
    //        // Execute the HTTP POST request
    //        HttpPost httpPost = new HttpPost(query);
    //        httpPost.addHeader("content-type", "application/json");
    //        StringEntity params;
    //        try {
    //            params = new StringEntity(gson.toJson(petRequest));
    //            httpPost.setEntity(params);
    //        } catch (UnsupportedEncodingException ex) {
    //            Logger.getLogger(PetStoreClient.class.getName()).log(Level.SEVERE, null, ex);
    //        }
    //        return executeHttpUriRequest(httpPost);
    //    }
    //
    //    private PetStoreResponse executeQueryPut(PetDTO petRequest, String queryPut) {
    //
    //        // Build the query for the REST service
    //        final String query = url + queryPut;
    //        log.info("[Query Put] : " + query);
    //
    //        // Execute the HTTP PUT request
    //        HttpPut httpPut = new HttpPut(query);
    //        httpPut.addHeader("content-type", "application/json");
    //        StringEntity params;
    //        try {
    //            params = new StringEntity(gson.toJson(petRequest));
    //            httpPut.setEntity(params);
    //        } catch (UnsupportedEncodingException ex) {
    //            Logger.getLogger(PetStoreClient.class.getName()).log(Level.SEVERE, null, ex);
    //        }
    //        return executeHttpUriRequest(httpPut);
    //    }
    //
    //    private PetStoreResponse executeHttpUriRequest(HttpUriRequest httpUriRequest) {
    //
    //        // Execute the HttpUriRequest
    //        try (CloseableHttpClient httpClient = HttpClients.createDefault();
    //                CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
    //            log.info("[Status Line] : " + response.getStatusLine());
    //            HttpEntity entity = response.getEntity();
    //            final String entityString = EntityUtils.toString(entity);
    //            log.info("[Entity] : " + entityString);
    //            PetStoreResponse petStoreResponse = gson.fromJson(entityString, PetStoreResponse.class);
    //            return petStoreResponse;
    //        } catch (IOException e) {
    //            log.info("IOException : " + e.toString());
    //            PetStoreResponse petStoreResponse = new PetStoreResponse();
    //            petStoreResponse.setSuccess(false);
    //            return petStoreResponse;
    //        } catch (JsonSyntaxException e) {
    //            log.info("JsonSyntaxException : " + e.toString());
    //            PetStoreResponse petStoreResponse = new PetStoreResponse();
    //            petStoreResponse.setSuccess(false);
    //            return petStoreResponse;
    //        }
    //    }
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
