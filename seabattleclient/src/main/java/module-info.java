module seabattleclient {
    requires socketmessaging;
    requires java.sql;
    requires java.logging;
    requires java.desktop;
    requires javafx.controls;
    requires slf4j.api;
    requires seabattledomain;
    requires seabattlecommon;

    exports seabattlegui;
    exports mocks;
    exports seabattlegame;
}