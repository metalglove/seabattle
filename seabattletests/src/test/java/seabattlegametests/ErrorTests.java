package seabattlegametests;

import messaging.messages.responses.ErrorResponse;
import messaging.messages.responses.RegisterResponse;
import mocks.MockClient;
import mocks.MockSeaBattleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorTests {
  private ISeaBattleGame game;
  private MockSeaBattleApplication application;
  private MockClient client;

  @BeforeEach
  public void setUp() {
    // Create the mock socket client
    client = new MockClient();

    // Create mock Sea Battle GUI for player
    application = new MockSeaBattleApplication();

    // Create the game
    game = new SeaBattleGame(application, client);
  }

  @Test()
  public void should_Set_ErrorMessage_When_An_ErrorResponse_With_NULL_As_ErrorMessage_Is_Received() {
    // Arrange
    String name = "Henk";
    String password = "Karel32";
    final boolean multiPlayerMode = true;
    game.registerPlayer(name, password, multiPlayerMode);
    client.setMockUpResponse(new RegisterResponse(1, true, null, null, duplicate));

    // Act
    client.setMockUpResponse(new ErrorResponse(null, "Henk", 1));

    // Assert
    assertEquals("Something happened server side. The game has ended.", application.getErrorMessage());
  }

  @Test()
  public void should_Set_ErrorMessage_When_An_ErrorResponse_Is_Received() {
    // Arrange
    String name = "Henk";
    String password = "Karel32";
    final boolean multiPlayerMode = true;
    game.registerPlayer(name, password, multiPlayerMode);
    client.setMockUpResponse(new RegisterResponse(1, true, null, null, duplicate));

    // Act
    client.setMockUpResponse(new ErrorResponse("The opponent crashed.", "Henk", 1));

    // Assert
    assertEquals("The opponent crashed.", application.getErrorMessage());
  }
}
