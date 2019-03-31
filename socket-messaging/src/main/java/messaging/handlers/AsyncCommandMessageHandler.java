package messaging.handlers;

import messaging.messages.Message;
import messaging.sockets.AsyncIdentifiableClientSocket;
import messaging.utilities.MessageLogger;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

import static java.lang.String.format;

public class AsyncCommandMessageHandler implements CompletionHandler<Void, Message> {
    private final AsyncIdentifiableClientSocket client;
    private final MessageLogger messageLogger;

    public AsyncCommandMessageHandler(AsyncIdentifiableClientSocket client, MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
        this.client = client;
    }

    @Override
    public void completed(Void result, Message attachment) {
        try {
            messageLogger.info(format("CommandMessage executed successfully: {%s} by {%s}", attachment.getClass().getSimpleName(), client.getChannel().getRemoteAddress().toString()));
        } catch (IOException e) {
            messageLogger.error(e.getMessage());
        }
    }

    @Override
    public void failed(Throwable exc, Message attachment) {
        try {
            messageLogger.error(format("CommandMessage executed unsuccessfully: {%s} by {%s}", attachment.getClass().getSimpleName(), client.getChannel().getRemoteAddress().toString()));
        } catch (IOException e) {
            messageLogger.error(e.getMessage());
        }
    }
}
