module socketmessaging {
  requires java.desktop;
  requires seabattlecommon;
  requires seabattledomain;

  exports messaging.messages.requests;
  exports messaging.messages.responses;
  exports messaging.messages;
  exports messaging.utilities;
  exports messaging.sockets;
  exports messaging.handlers;
  exports messaging.interfaces;
}