package dtos.responses;

public class RegisterResponseDto {
    private Integer id;
    private String username;
    private boolean success;
    private String message;

    public RegisterResponseDto() {
    }

    public RegisterResponseDto(Integer id, String username, boolean success, String message) {
        this.id = id;
        this.username = username;
        this.success = success;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
