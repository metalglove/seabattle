package rest;

import daldtos.UserResultDto;

public class SeaBattleResponse {

    // Indicates whether REST call was successful
    private boolean success;

    private String responseString;

    public boolean isSuccess(){ return success; }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponse(){ return responseString; }

    public void setResponse(String response) { this.responseString = response; }

    public UserResultDto user;
}
