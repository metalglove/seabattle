module socketmessaging {
    requires seabattledomain;
    requires java.desktop;

    exports messaging.handlers;
    exports messaging.interfaces;
    exports messaging.messages.requests;
    exports messaging.messages.responses;
    exports messaging.messages;
    exports messaging.utilities;
    exports messaging.sockets;
}