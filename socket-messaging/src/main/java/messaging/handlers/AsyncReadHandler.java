package messaging.handlers;

import messaging.interfaces.ReadableSocket;
import messaging.messages.Message;
import messaging.utilities.MessageConverter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AsyncReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final ReadableSocket readableSocket;

    public AsyncReadHandler(ReadableSocket readableSocket) {
        this.readableSocket = readableSocket;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        readableSocket.startReading();
        System.out.println("Successfully read message!");
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try {
            Message message = MessageConverter.convertFromBytes(bytes);
            readableSocket.addMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to read message (while converting)!");
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("Failed to read message!");
    }
}