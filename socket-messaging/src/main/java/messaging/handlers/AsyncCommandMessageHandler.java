package messaging.handlers;

import messaging.messages.Message;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncCommandMessageHandler implements CompletionHandler<Void, Message> {
    private final AsyncIdentifiableClientSocket client;

    public AsyncCommandMessageHandler(AsyncIdentifiableClientSocket client) {
        this.client = client;
    }

    @Override
    public void completed(Void result, Message attachment) {
        try {
            System.out.println(format("CommandMessage executed successfully: {%s} by {%s}", attachment.getClass().getSimpleName(), client.getChannel().getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Message attachment) {
        try {
            System.out.println(format("CommandMessage executed unsuccessfully: {%s} by {%s}", attachment.getClass().getSimpleName(), client.getChannel().getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
