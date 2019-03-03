module seabattleclient {
  requires socketmessaging;
  requires gson;
  requires java.sql;
  requires java.logging;
  requires java.desktop;
  requires javafx.controls;
  requires slf4j.api;
    requires seabattledomain;

    exports seabattlegui;
}