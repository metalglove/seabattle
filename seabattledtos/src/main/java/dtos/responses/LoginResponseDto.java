package dtos.responses;

public class LoginResponseDto {
  private Long id;
  private String username;
  private boolean success;

  public LoginResponseDto() {

  }

  public LoginResponseDto(Long id, String username, boolean success) {
    this.id = id;
    this.username = username;
    this.success = success;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean getSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
