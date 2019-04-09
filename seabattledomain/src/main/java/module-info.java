module seabattledomain {
  requires seabattlecommon;
  requires java.persistence;

  exports domain;
  exports domain.ships;
  exports domain.actions;
  exports domain.interfaces;
  exports domain.cryptography;
}