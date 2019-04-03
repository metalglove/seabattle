import daldtos.UserResultDto;

public class SeaBattleResponse {

    // Indicates whether REST call was successful
    private boolean success;

    private String responseString;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setResponse(String response) { this.responseString = response; }

    public UserResultDto user;
}
