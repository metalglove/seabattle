package seabattleserver;

import interfaces.ISeaBattleServerRest;
import seabattleserverrest.SeaBattleServerRest;
import interfaces.ISeaBattleGameService;
import services.SeaBattleGameService;
import utilities.ShipFactory;

import java.io.IOException;

public class SeaBattleServer {
    public static void main(String[] args) throws IOException {
        ISeaBattleServerRest rest = new SeaBattleServerRest();
        ISeaBattleGameService gameService = new SeaBattleGameService(new ShipFactory());
        Server server = new Server(9999, rest, gameService);
        try {
            server.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
