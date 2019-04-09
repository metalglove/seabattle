module seabattlerest {
    requires seabattledtos;
    requires spring.web;
    requires spring.beans;
    requires spring.context;
    requires spring.webmvc;
    requires spring.data.jpa;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires seabattledomain;
    requires jackson.dataformat.yaml;
}