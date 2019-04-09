package dtos.requests;

public class RegisterRequestDto {
    private String username;
    private char[] secret;

    public RegisterRequestDto() {

    }

    public RegisterRequestDto(String username, char[] secret) {
        this.username = username;
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public char[] getSecret() {
        return secret;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecret(char[] secret) {
        this.secret = secret;
    }

}
