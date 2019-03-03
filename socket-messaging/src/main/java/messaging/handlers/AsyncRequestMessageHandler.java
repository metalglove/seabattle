package messaging.handlers;

import messaging.interfaces.WritingSocket;
import messaging.messages.Message;
import messaging.sockets.AsyncIdentifiableClientSocket;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncRequestMessageHandler implements CompletionHandler<Message, Message> {
    private final WritingSocket server;
    private final AsyncIdentifiableClientSocket client;

    public AsyncRequestMessageHandler(WritingSocket server, AsyncIdentifiableClientSocket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void completed(Message response, Message request) {
        try {
            System.out.println(format("RequestMessage executed successfully: {%s}, {%s} by {%s}", request.getClass().getName(), response.getClass().getName(), client.getChannel().getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.startWriting(client, response);
    }

    @Override
    public void failed(Throwable exc, Message request) {
        try {
            System.out.println(format("RequestMessage executed unsuccessfully: {%s} by {%s}", request.getClass().getName(), client.getChannel().getRemoteAddress().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}