module seabattleserver {
  requires socketmessaging;
  requires seabattledomain;
  requires seabattledtos;
  requires org.yaml.snakeyaml;
  requires httpclient;
  requires httpcore;
  requires seabattlecommon;
  exports interfaces;
}