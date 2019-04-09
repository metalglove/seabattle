package seabattlegame.listeners;

import common.MessageLogger;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.ErrorResponse;
import seabattlegame.ISeaBattleGame;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ErrorResponseChangeListener implements PropertyChangeListener {
  private final ISeaBattleGUI application;
  private final ISeaBattleGame game;
  private final ObservableClientSocket client;
  private final MessageLogger messageLogger;

  public ErrorResponseChangeListener(ISeaBattleGUI application, ISeaBattleGame game, ObservableClientSocket client, MessageLogger messageLogger) {
    this.application = application;
    this.game = game;
    this.client = client;
    this.messageLogger = messageLogger;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    ErrorResponse response = (ErrorResponse) evt.getNewValue();
    if (response.getErrorMessage() != null) {
      application.showErrorMessage(response.getErrorMessage());
      messageLogger.error("ErrorResponse resulted in: " + response.getErrorMessage());
      game.resetGame();
    } else {
      application.showErrorMessage("Something happened server side. The game has ended.");
      messageLogger.error("ErrorResponse resulted in: " + "Something happened server side. Try playing again later.");
    }
    application.resetGUI();
    client.removeListener(ErrorResponse.class.getSimpleName(), this);
  }
}