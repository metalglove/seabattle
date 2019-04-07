package rest;

import com.google.gson.Gson;
import common.MessageLogger;
import daldtos.UserResultDto;

class RestResponseHelper {
    private static final MessageLogger messageLogger = new MessageLogger("REST-RESPONSE-HELPER");
    private static final Gson gson = new Gson();

    static String getErrorResponseString()
    {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(false);
        String output = gson.toJson(response);
        messageLogger.info(output);
        return output;
    }

    static String getSuccessResponse(boolean success){
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(success);
        String output = gson.toJson(response);
        messageLogger.info(output);
        return output;
    }
    static String getRegisterResponse(boolean success)
    {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(success);
        response.setResponse("Registered with name: " + response.user.username);
        String output = gson.toJson(response);
        messageLogger.info(output);
        return output;
    }
    static String getLoginResponse(UserResultDto user)
    {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(true);
        response.setResponse("Login with name: " + user.username);
        String output = gson.toJson(response);
        messageLogger.info(output);
        return output;
    }

    static String getUsernameInUseResponse(boolean inUse) {
        SeaBattleResponse response = new SeaBattleResponse();
        response.setSuccess(inUse);
        response.setResponse("Username in use: " + inUse);
        String output = gson.toJson(response);
        messageLogger.info(output);
        return output;
    }
}
