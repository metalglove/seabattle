package mocks;

import common.MessageLogger;
import messaging.interfaces.ObservableClientSocket;
import messaging.messages.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class MockClient implements ObservableClientSocket {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final MessageLogger messageLogger = new MessageLogger("MOCK-CLIENT");
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    private Message response = null;

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(eventName, listener);
        messageLogger.info(format("New listener count after adding {%s}, count: %s", listener.getClass().getSimpleName(), propertyChangeSupport.getPropertyChangeListeners().length));
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(eventName, listener);
        messageLogger.info(format("New listener count after removing {%s}, count: %s", listener.getClass().getSimpleName(), propertyChangeSupport.getPropertyChangeListeners().length));
    }

    @Override
    public void startReading() {
        messageLogger.info("Started reading...");
    }

    @Override
    public void addMessage(Message message) {
        String messageType = message.getClass().getSimpleName();
        propertyChangeSupport.firePropertyChange(messageType, null, message);
        messageLogger.info(format("Successfully received {%s} message!", messageType));
        messages.add(message);
    }

    @Override
    public void startWriting(Message message) {
        messageLogger.info("Started writing...");
        messageLogger.info(format("Sent {%s}", message.getClass().getSimpleName()));
    }

    public Message getResponse() {
        return response;
    }

    public void setMockUpResponse(Message response) {
        this.response = response;
        addMessage(response);
    }
}
