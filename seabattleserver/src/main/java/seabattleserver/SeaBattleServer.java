package seabattleserver;

import seabattleserverrest.ISeaBattleServerRest;
import seabattleserverrest.SeaBattleServerRest;

import java.io.IOException;

public class SeaBattleServer {
    public static void main(String[] args) throws IOException {
        ISeaBattleServerRest rest = new SeaBattleServerRest();
        Server server = new Server(9999, rest);
        try {
            server.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
