package seabattleserver;

import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import messaging.utilities.MessageLogger;
import seabattleserverrest.SeaBattleServerRest;
import services.SeaBattleGameService;
import utilities.ShipFactory;

import java.io.IOException;

public class SeaBattleServer {
    public static void main(String[] args) throws IOException {
        ISeaBattleServerRest rest = new SeaBattleServerRest();
        MessageLogger messageLogger = new MessageLogger("SERVER");
        ISeaBattleGameService gameService = new SeaBattleGameService(new ShipFactory(), messageLogger);
        Server server = new Server(9999, rest, gameService, messageLogger);
        try {
            server.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
