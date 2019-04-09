module seabattleclient {
  requires socketmessaging;
  requires java.sql;
  requires java.logging;
  requires java.desktop;
  requires seabattledomain;
  requires seabattlecommon;
  requires javafx.graphics;
  requires javafx.controls;

  exports seabattlegui;
  exports seabattlegame;
}