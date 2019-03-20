package seabattlegame;

import messaging.handlers.AsyncReadHandler;
import messaging.handlers.AsyncWriteHandler;
import messaging.messages.Message;
import messaging.sockets.AsyncClientSocket;
import messaging.utilities.MessageConverter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class Client extends AsyncClientSocket {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Client(String host, int port) throws IOException {
        super(host, port);
    }

    public void addListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(eventName, listener);
        System.out.println("New listener count after an add: " + propertyChangeSupport.getPropertyChangeListeners().length);
    }

    public void removeListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(eventName, listener);
        System.out.println("New listener count after a remove: " + propertyChangeSupport.getPropertyChangeListeners().length);
    }

    @Override
    public void startReading() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        channel.read(byteBuffer, byteBuffer, new AsyncReadHandler(this));
    }

    @Override
    public void addMessage(Message message) {
        String messageType = message.getClass().getSimpleName();
        propertyChangeSupport.firePropertyChange(messageType, null, message);
        System.out.printf("Successfully received {%s} message!%n", messageType);
    }

    @Override
    public List<Message> getMessages() {
        return null;
    }

    @Override
    public void startWriting(Message message) {
        final ByteBuffer buffer = ByteBuffer.allocate(2048);
        try {
            buffer.put(MessageConverter.convertToBytes(message));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        buffer.flip();
        channel.write(buffer, channel, new AsyncWriteHandler());
    }
}
