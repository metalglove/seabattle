module seabattleclient {
    requires socketmessaging;
    requires java.sql;
    requires java.logging;
    requires java.desktop;
    requires javafx.controls;
    requires seabattledomain;
    requires seabattlecommon;

    exports seabattlegui;
    exports seabattlegame;
}