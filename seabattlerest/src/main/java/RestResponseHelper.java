import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RestResponseHelper {
    private static final Logger log = LoggerFactory.getLogger(SeaBattleRESTService.class);
    private static final Gson gson = new Gson();

    static String getErrorResponseString()
    {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(false);
        String output = gson.toJson(response);
        log.info("[Server response] " + output);
        return output;
    }

    static String getSuccessResponse(boolean success){
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(success);
        String output = gson.toJson(response);
        log.info("[Server response] " + output);
        return output;
    }
    static String getRegisterResponse(boolean success)
    {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(success);
        response.setResponse("Register test triggered");
        String output = gson.toJson(response);
        log.info("[Server response] " + output);
        return output;
    }
    static String getLoginResponse(UserResultDto user)
    {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(true);
        response.setResponse("Login test triggered");
        String output = gson.toJson(response);
        log.info("[Server response] " + output);
        return output;
    }

    static String getUsernameInUseResponse(boolean inUse) {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(inUse);
        response.setResponse("Username in use triggered");
        String output = gson.toJson(response);
        log.info("[Server response] " + output);
        return output;
    }
}
