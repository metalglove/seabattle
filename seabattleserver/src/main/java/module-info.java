module seabattleserver {
    requires socketmessaging;
    requires seabattledomain;
    requires seabattledtos;
    requires seabattlecommon;
    requires seabattlerest;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires gson;
    exports interfaces;
}