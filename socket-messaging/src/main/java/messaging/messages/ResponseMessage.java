package messaging.messages;

public class ResponseMessage extends Message {
    public final String request;
    public final String data;
    public final boolean success;

    public ResponseMessage(String request, String data, boolean success) {

        this.request = request;
        this.data = data;
        this.success = success;
    }
}