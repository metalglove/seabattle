package integrationtests;

import common.MessageLogger;
import domain.Ship;
import domain.ShipCreationArgument;
import domain.ShipFactory;
import domain.interfaces.IFactoryWithArgument;
import interfaces.ISeaBattleGameService;
import interfaces.ISeaBattleServerRest;
import mocks.MockSeaBattleApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;
import seabattleserver.Server;
import seabattleserverrest.SeaBattleServerRest;
import services.SeaBattleGameService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTests {
    private ISeaBattleGame game;
    private MockSeaBattleApplication application;
    private ISeaBattleServerRest rest;
    private ISeaBattleGameService gameService;
    private Client client;
    private Server server;
    private IFactoryWithArgument<Ship, ShipCreationArgument> shipFactory;

    @SuppressWarnings("Duplicates")
    @BeforeEach
    public void setUp() {
        // Start up server
        rest = new SeaBattleServerRest();
        shipFactory = new ShipFactory();
        gameService = new SeaBattleGameService(shipFactory, new MessageLogger("GAME-SERVICE"));

        ExecutorService exService = Executors.newSingleThreadExecutor();
        Runnable task = () -> {
            try {
                server = new Server(9999, rest, gameService, new MessageLogger("SERVER"));
                server.await();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        exService.execute(task);

        // Start up client 1
        try {
            client = new Client("127.0.0.1", 9999, new MessageLogger("CLIENT"));
            client.connect();
            client.ensureConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create mock Sea Battle GUI for player
        application = new MockSeaBattleApplication();

        // Create the game
        game = new SeaBattleGame(application, client);
    }

    @Test()
    public void should_Set_PlayerName_To_Henk_On_Application_When_Name_Is_Karel_And_Password_Is_Karel32_And_MultiPlayer_Is_False() {
        // Arrange
        String name = "Henk";
        String password = "Karel32";
        final boolean multiPlayerMode = false;

        // Act
        game.registerPlayer(name, password, multiPlayerMode);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals("Henk", application.getPlayerName());
    }
}
