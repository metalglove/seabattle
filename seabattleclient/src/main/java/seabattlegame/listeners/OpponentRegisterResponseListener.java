package seabattlegame.listeners;

import common.MessageLogger;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.responses.OpponentRegisterResponse;
import seabattlegui.ISeaBattleGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OpponentRegisterResponseListener implements PropertyChangeListener {
    private final ISeaBattleGUI application;
    private final ObservableClientSocket client;
    private final MessageLogger messageLogger;

    public OpponentRegisterResponseListener(ISeaBattleGUI application, ObservableClientSocket client, MessageLogger messageLogger) {
        this.application = application;
        this.client = client;
        this.messageLogger = messageLogger;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        OpponentRegisterResponse response = (OpponentRegisterResponse) evt.getNewValue();
        if (!response.isSuccess()) {
            application.showErrorMessage("Opponent registered but server faulted!");
            messageLogger.error("Opponent registered but server faulted!");
        } else {
            application.setOpponentName(response.getOpponentNumber(), response.getOpponentName());
            client.removeListener(OpponentRegisterResponse.class.getSimpleName(), this);
        }
    }
}