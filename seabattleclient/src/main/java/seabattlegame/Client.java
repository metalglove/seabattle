package seabattlegame;

import messaging.handlers.AsyncReadHandler;
import messaging.handlers.AsyncWriteHandler;
import messaging.messages.Message;
import messaging.sockets.AsyncClientSocket;
import messaging.utilities.MessageConverter;
import messaging.utilities.MessageLogger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static java.lang.String.format;

public class Client extends AsyncClientSocket {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final MessageLogger messageLogger;

    public Client(String host, int port, MessageLogger messageLogger) throws IOException {
        super(host, port);
        this.messageLogger = messageLogger;
    }

    public void addListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(eventName, listener);
        messageLogger.info(format("New listener count after adding {%s}, count: %s", listener.getClass().getSimpleName(), propertyChangeSupport.getPropertyChangeListeners().length));
    }

    public void removeListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(eventName, listener);
        messageLogger.info(format("New listener count after removing {%s}, count: %s", listener.getClass().getSimpleName(), propertyChangeSupport.getPropertyChangeListeners().length));
    }

    @Override
    public void startReading() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        channel.read(byteBuffer, byteBuffer, new AsyncReadHandler(this, socketMessageLogger));
    }

    @Override
    public void addMessage(Message message) {
        String messageType = message.getClass().getSimpleName();
        propertyChangeSupport.firePropertyChange(messageType, null, message);
        messageLogger.info(format("Successfully received {%s} message!", messageType));
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
            messageLogger.error(e.getMessage());
            return;
        }
        buffer.flip();
        channel.write(buffer, channel, new AsyncWriteHandler(socketMessageLogger));
    }

    public void ensureConnection() {
        try {
            messageLogger.overwritableInfo("");
            int i = 1;
            while(channel.getRemoteAddress() == null) {
                messageLogger.overwritableInfo(format("Connecting %s...", i++));
            }
            messageLogger.overwritableInfo(format("Ensured connection in %s remote checks! \n", i));
        } catch (IOException e) {
            messageLogger.error("Connection is not open! " + e.getMessage());
        }
    }
}
