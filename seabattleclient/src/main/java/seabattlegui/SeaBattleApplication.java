/*
 * Sea Battle Start project.
 */
package seabattlegui;

import common.MessageLogger;
import domain.Point;
import domain.Ship;
import domain.ShipType;
import domain.ShotType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import seabattlegame.Client;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;


/**
 * Main application of the sea battle game.
 *
 * @author Nico Kuijpers
 */
@SuppressWarnings("Duplicates")
public class SeaBattleApplication extends Application implements ISeaBattleGUI {

    //private static final Logger log = LoggerFactory.getLogger(SeaBattleApplication.class);

    private static final MessageLogger messageLogger = new MessageLogger("APPLICATION");
    // Constants to define size of GUI elements
    private final int BORDERSIZE = 10; // Size of borders in pixels
    private final int AREAWIDTH = 400; // Width of area in pixels
    private final int AREAHEIGHT = AREAWIDTH; // Height of area in pixels
    private final int SQUAREWIDTH = 36; // Width of single square in pixels
    private final int SQUAREHEIGHT = 36; // Height of single square in pixels
    private final int BUTTONWIDTH = 180; // Width of button

    // Constants to define number of squares horizontal and vertical
    private final int NRSQUARESHORIZONTAL = 10;
    private final int NRSQUARESVERTICAL = 10;

    // Opponent's name
    private String opponentName;

    // Label for opponent's name
    private Label labelOpponentName;

    // Target area, a 10 x 10 grid where the opponent's ships are placed
    private Rectangle targetArea;

    // Squares for the target area
    private Rectangle[][] squaresTargetArea;

    // Player's number (to be determined by the sea battle game)
    int playerNr = 0;

    // Player's name
    private String playerName = null;

    // Player that may fire a shot (player 0 or player 1)
    //private int playerTurn = 0;

    // Label for player's name
    private Label labelPlayerName;

    // Text field to set player's name
    private Label labelYourName;
    private TextField textFieldPlayerName;

    // Password field to set player's password
    private Label labelYourPassword;
    private PasswordField passwordFieldPlayerPassword;

    // Ocean area, a 10 x 10 grid where the player's ships are placed
    private Rectangle oceanArea;

    // Squares for the ocean area
    private Rectangle[][] squaresOceanArea;

    private List<Ship> playerShips;

    // Sea battle game
    private ISeaBattleGame game;

    // Flag to indicate whether game is in single-player or multiplayer mode
    private boolean singlePlayerMode = true;

    // Radio buttons to indicate whether game is in single-player or multiplayer mode
    private RadioButton radioSinglePlayer;
    private RadioButton radioMultiPlayer;

    // Flag to indicate whether the game is in playing mode
    private boolean playingMode = false;

    // Flag to indicate that the game is ended
    private boolean gameEnded = false;

    // Flag to indicate whether next ship should be placed horizontally or vertically
    private boolean horizontal = true;

    // Radio buttons to indicate whether next ship should be placed horizontally or vertically
    private Label labelHorizontalVertical;
    private RadioButton radioHorizontal;
    private RadioButton radioVertical;

    // Buttons to register player, start the game, and place or remove ships
    private Button buttonRegisterPlayer;
    Button buttonPlaceAllShips;
    Button buttonRemoveAllShips;
    Button buttonReadyToPlay;
    Button buttonStartNewGame;
    Button buttonPlaceAircraftCarrier;
    Button buttonPlaceBattleShip;
    Button buttonPlaceCruiser;
    Button buttonPlaceSubmarine;
    Button buttonPlaceMineSweeper;
    Button buttonRemoveShip;

    // Flag to indicate whether square is selected in ocean area
    private boolean squareSelectedInOceanArea = false;

    // X and y-position of selected square in ocean region
    private int selectedSquareX;
    private int selectedSquareY;
    // for returning color on previous selected square
    private Color oldColorPress = null;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        messageLogger.info("Seabattle started");

        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(BORDERSIZE);
        grid.setVgap(BORDERSIZE);
        grid.setPadding(new Insets(BORDERSIZE, BORDERSIZE, BORDERSIZE, BORDERSIZE));

        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);

        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, AREAWIDTH + BUTTONWIDTH + 3 * BORDERSIZE, 2 * AREAHEIGHT + 2 * BORDERSIZE + 65);
        root.getChildren().add(grid);

        // Label for opponent's name
        opponentName = "Opponent";
        labelOpponentName = new Label(opponentName + "\'s grid");
        labelOpponentName.setMinWidth(AREAWIDTH);
        grid.add(labelOpponentName, 0, 0, 1, 2);

        // Target area, a 10 x 10 grid where the opponent's ships are placed
        targetArea = new Rectangle(BORDERSIZE, 3 * BORDERSIZE, AREAWIDTH, AREAHEIGHT);
        targetArea.setFill(Color.WHITE);
        root.getChildren().add(targetArea);

        // Create 10 x 10 squares for the target area
        squaresTargetArea = new Rectangle[NRSQUARESHORIZONTAL][NRSQUARESVERTICAL];
        for (int i = 0; i < NRSQUARESHORIZONTAL; i++) {
            for (int j = 0; j < NRSQUARESVERTICAL; j++) {
                double x = targetArea.getX() + i * (AREAWIDTH / NRSQUARESHORIZONTAL) + 2;
                double y = targetArea.getY() + j * (AREAHEIGHT / NRSQUARESVERTICAL) + 2;
                Rectangle rectangle = new Rectangle(x, y, SQUAREWIDTH, SQUAREHEIGHT);
                rectangle.setArcWidth(10.0);
                rectangle.setArcHeight(10.0);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setVisible(true);
                final int xpos = i;
                final int ypos = j;
                rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                rectangleTargetAreaMousePressed(event, xpos, ypos);
                            }
                        });
                squaresTargetArea[i][j] = rectangle;
                root.getChildren().add(rectangle);
            }
        }

        // Label for player's name
        playerName = "";
        labelPlayerName = new Label("Your grid");
        labelPlayerName.setMinWidth(AREAWIDTH);
        grid.add(labelPlayerName, 0, 33, 1, 2);

        // Ocean area, a 10 x 10 grid where the player's ships are placed
        oceanArea = new Rectangle(BORDERSIZE, 46 * BORDERSIZE, AREAWIDTH, AREAHEIGHT);
        oceanArea.setFill(Color.WHITE);
        root.getChildren().add(oceanArea);

        // Create 10 x 10 squares for the ocean area
        squaresOceanArea = new Rectangle[NRSQUARESHORIZONTAL][NRSQUARESVERTICAL];
        for (int i = 0; i < NRSQUARESHORIZONTAL; i++) {
            for (int j = 0; j < NRSQUARESVERTICAL; j++) {
                double x = oceanArea.getX() + i * (AREAWIDTH / NRSQUARESHORIZONTAL) + 2;
                double y = oceanArea.getY() + j * (AREAHEIGHT / NRSQUARESVERTICAL) + 2;
                Rectangle rectangle = new Rectangle(x, y, SQUAREWIDTH, SQUAREHEIGHT);
                rectangle.setArcWidth(10.0);
                rectangle.setArcHeight(10.0);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setVisible(true);
                final int xpos = i;
                final int ypos = j;
                rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                rectangleOceanAreaMousePressed(event, xpos, ypos);
                            }
                        });
                squaresOceanArea[i][j] = rectangle;
                root.getChildren().add(rectangle);
            }
        }

        // Text field to set the player's name
        labelYourName = new Label("Your name:");
        grid.add(labelYourName, 1, 2, 1, 2);
        textFieldPlayerName = new TextField();
        textFieldPlayerName.setMinWidth(BUTTONWIDTH);
        textFieldPlayerName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerName = textFieldPlayerName.getText();
                labelPlayerName.setText(playerName + "\'s grid");
            }
        });
        grid.add(textFieldPlayerName, 1, 4, 1, 2);

        // Text field to set the player's password
        labelYourPassword = new Label("Your password:");
        grid.add(labelYourPassword, 1, 6, 1, 2);
        passwordFieldPlayerPassword = new PasswordField();
        passwordFieldPlayerPassword.setMinWidth(BUTTONWIDTH);
        grid.add(passwordFieldPlayerPassword, 1, 8, 1, 2);

        // Radio buttons to choose between single-player and multi-player mode
        radioSinglePlayer = new RadioButton("single-player mode");
        Tooltip tooltipSinglePlayer = new Tooltip("Play game in single-player mode");
        radioSinglePlayer.setTooltip(tooltipSinglePlayer);
        radioSinglePlayer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                singlePlayerMode = true;
            }
        });
        radioMultiPlayer = new RadioButton("multi-player mode");
        Tooltip tooltipMultiPlayer = new Tooltip("Play game in multi-player mode");
        radioMultiPlayer.setTooltip(tooltipMultiPlayer);
        radioMultiPlayer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                singlePlayerMode = false;
            }
        });
        ToggleGroup tgSingleMultiPlayer = new ToggleGroup();
        radioSinglePlayer.setToggleGroup(tgSingleMultiPlayer);
        radioMultiPlayer.setToggleGroup(tgSingleMultiPlayer);
        radioSinglePlayer.setSelected(true);
        grid.add(radioSinglePlayer, 1, 10, 1, 2);
        grid.add(radioMultiPlayer, 1, 12, 1, 2);

        // Button to register the player
        buttonRegisterPlayer = new Button("Register");
        buttonRegisterPlayer.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRegisterParticipant =
                new Tooltip("Press this button to register as player");
        buttonRegisterPlayer.setTooltip(tooltipRegisterParticipant);
        buttonRegisterPlayer.setOnAction(
                (EventHandler<ActionEvent>) event -> {
                    try {
                        registerPlayer();
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageLogger.error(format("Register Player error: %s", e.getMessage()));
                    }
                });
        grid.add(buttonRegisterPlayer, 1, 14, 1, 3);

        // Button to place the player's ships automatically
        buttonPlaceAllShips = new Button("Place ships for me");
        buttonPlaceAllShips.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceShips =
                new Tooltip("Press this button to let the computer place your ships");
        buttonPlaceAllShips.setTooltip(tooltipPlaceShips);
        buttonPlaceAllShips.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipsAutomatically();
            }
        });
        buttonPlaceAllShips.setDisable(true);
        grid.add(buttonPlaceAllShips, 1, 18, 1, 3);

        // Button to remove the player's ships that are already placed
        buttonRemoveAllShips = new Button("Remove all my ships");
        buttonRemoveAllShips.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRemoveAllShips =
                new Tooltip("Press this button to remove all your ships");
        buttonRemoveAllShips.setTooltip(tooltipRemoveAllShips);
        buttonRemoveAllShips.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                removeAllShips();
            }
        });
        buttonRemoveAllShips.setDisable(true);
        grid.add(buttonRemoveAllShips, 1, 22, 1, 3);

        // Button to notify that the player is ready to start playing
        buttonReadyToPlay = new Button("Ready to play");
        buttonReadyToPlay.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipReadyToPlay =
                new Tooltip("Press this button when you are ready to play");
        buttonReadyToPlay.setTooltip(tooltipReadyToPlay);
        buttonReadyToPlay.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                notifyWhenReady();
            }
        });
        buttonReadyToPlay.setDisable(true);
        grid.add(buttonReadyToPlay, 1, 26, 1, 3);

        // Button to start a new game
        buttonStartNewGame = new Button("Start new game");
        buttonStartNewGame.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipStartNewGame =
                new Tooltip("Press this button to start a new game");
        buttonStartNewGame.setTooltip(tooltipStartNewGame);
        buttonStartNewGame.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                startNewGame();
            }
        });
        buttonStartNewGame.setDisable(true);
        grid.add(buttonStartNewGame, 1, 30, 1, 3);

        // Radio buttons to place ships horizontally or vertically
        labelHorizontalVertical = new Label("Place next ship: ");
        radioHorizontal = new RadioButton("horizontally");
        Tooltip tooltipHorizontal = new Tooltip("Place next ship horizontally");
        radioHorizontal.setTooltip(tooltipHorizontal);
        radioHorizontal.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                horizontal = true;
            }
        });
        radioVertical = new RadioButton("vertically");
        Tooltip tooltipVertical = new Tooltip("Place next ship vertically");
        radioVertical.setTooltip(tooltipVertical);
        radioVertical.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                horizontal = false;
            }
        });
        ToggleGroup tgHorizontalVertical = new ToggleGroup();
        radioHorizontal.setToggleGroup(tgHorizontalVertical);
        radioVertical.setToggleGroup(tgHorizontalVertical);
        radioHorizontal.setSelected(true);
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        grid.add(labelHorizontalVertical, 1, 36, 1, 2);
        grid.add(radioHorizontal, 1, 38, 1, 2);
        grid.add(radioVertical, 1, 40, 1, 2);

        // Button to place aircraft carrier on selected square
        buttonPlaceAircraftCarrier = new Button("Place aircraft carrier (5)");
        buttonPlaceAircraftCarrier.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceAircraftCarrier =
                new Tooltip("Press this button to place the aircraft carrier on the selected square");
        buttonPlaceAircraftCarrier.setTooltip(tooltipPlaceAircraftCarrier);
        buttonPlaceAircraftCarrier.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipAtSelectedSquare(ShipType.AIRCRAFTCARRIER, horizontal);
            }
        });
        buttonPlaceAircraftCarrier.setDisable(true);
        grid.add(buttonPlaceAircraftCarrier, 1, 42, 1, 3);

        // Button to place battle ship on selected square
        buttonPlaceBattleShip = new Button("Place battle ship (4)");
        buttonPlaceBattleShip.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceBattleShip =
                new Tooltip("Press this button to place the battle ship on the selected square");
        buttonPlaceBattleShip.setTooltip(tooltipPlaceBattleShip);
        buttonPlaceBattleShip.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipAtSelectedSquare(ShipType.BATTLESHIP, horizontal);
            }
        });
        buttonPlaceBattleShip.setDisable(true);
        grid.add(buttonPlaceBattleShip, 1, 46, 1, 3);

        // Button to place battle ship on selected square
        buttonPlaceCruiser = new Button("Place cruiser (3)");
        buttonPlaceCruiser.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceCruiser =
                new Tooltip("Press this button to place the cruiser on the selected square");
        buttonPlaceCruiser.setTooltip(tooltipPlaceCruiser);
        buttonPlaceCruiser.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipAtSelectedSquare(ShipType.CRUISER, horizontal);
            }
        });
        buttonPlaceCruiser.setDisable(true);
        grid.add(buttonPlaceCruiser, 1, 50, 1, 3);

        // Button to place mine sweeper on selected square
        buttonPlaceSubmarine = new Button("Place submarine (3)");
        buttonPlaceSubmarine.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceSubmarine =
                new Tooltip("Press this button to place the submarine on the selected square");
        buttonPlaceSubmarine.setTooltip(tooltipPlaceSubmarine);
        buttonPlaceSubmarine.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipAtSelectedSquare(ShipType.SUBMARINE, horizontal);
            }
        });
        buttonPlaceSubmarine.setDisable(true);
        grid.add(buttonPlaceSubmarine, 1, 54, 1, 3);

        // Button to place mine sweeper on selected square
        buttonPlaceMineSweeper = new Button("Place mine sweeper (2)");
        buttonPlaceMineSweeper.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceMineSweeper =
                new Tooltip("Press this button to place the mine sweeper on the selected square");
        buttonPlaceMineSweeper.setTooltip(tooltipPlaceMineSweeper);
        buttonPlaceMineSweeper.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                placeShipAtSelectedSquare(ShipType.MINESWEEPER, horizontal);
            }
        });
        buttonPlaceMineSweeper.setDisable(true);
        grid.add(buttonPlaceMineSweeper, 1, 58, 1, 3);

        // Button to remove ship that is positioned at selected square
        buttonRemoveShip = new Button("Remove ship");
        buttonRemoveShip.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRemoveShip =
                new Tooltip("Press this button to remove ship that is "
                        + "positioned on the selected square");
        buttonRemoveShip.setTooltip(tooltipRemoveShip);
        buttonRemoveShip.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                removeShipAtSelectedSquare();
            }
        });
        buttonRemoveShip.setDisable(true);
        grid.add(buttonRemoveShip, 1, 62, 1, 3);

        // Set font for all labeled objects
        for (Node n : grid.getChildren()) {
            if (n instanceof Labeled) {
                ((Labeled) n).setFont(new Font("Arial", 13));
            }
        }

        playerShips = new ArrayList<>();
        // Define title and assign the scene for main window
        primaryStage.setTitle("Sea battle: the game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Set player number.
     *
     * @param playerNr identification of player
     * @param name     player's name
     */
    @Override
    public void setPlayerNumber(int playerNr, String name) {
        // Check identification of player
        //if (!this.playerName.equals(name)) {
        //    showMessage("ERROR: Wrong player name method setPlayerNumber()");
        //    return;
        //}
        this.playerNr = playerNr;
        messageLogger.info(format("Player number is: %s ", playerNr));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelPlayerName.setText(playerName + "\'s grid");
                labelYourName.setDisable(true);
                textFieldPlayerName.setDisable(true);
                labelYourPassword.setDisable(true);
                passwordFieldPlayerPassword.setDisable(true);
                radioSinglePlayer.setDisable(true);
                radioMultiPlayer.setDisable(true);
                buttonRegisterPlayer.setDisable(true);
                labelHorizontalVertical.setDisable(false);
                radioHorizontal.setDisable(false);
                radioVertical.setDisable(false);
                buttonPlaceAllShips.setDisable(false);
                buttonRemoveAllShips.setDisable(false);
                buttonReadyToPlay.setDisable(false);
                buttonPlaceAircraftCarrier.setDisable(false);
                buttonPlaceBattleShip.setDisable(false);
                buttonPlaceCruiser.setDisable(false);
                buttonPlaceSubmarine.setDisable(false);
                buttonPlaceMineSweeper.setDisable(false);
                buttonRemoveShip.setDisable(false);
            }
        });
        showMessage("Player " + name + " registered");
    }

    /**
     * Set the name of the opponent.
     * The opponent's name will be shown above the target area.
     *
     * @param playerNr identification of player
     * @param name     opponent's name
     */
    @Override
    public void setOpponentName(int playerNr, String name) {
        // Check identification of player
        //if (playerNr != this.playerNr) {
         //   showMessage("ERROR: Wrong player number method setOpponentName()");
         //   return;
        //}
        showMessage("Your opponent is " + name);
        opponentName = name;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelOpponentName.setText(opponentName + "\'s grid");
            }
        });
    }

    /**
     * Notification that the game has started.
     *
     * @param playerNr identification of player
     */
    @Override
    public void notifyStartGame(int playerNr) {
        // Check identification of player
        //if (playerNr != this.playerNr) {
        //    showMessage("ERROR: Wrong player number method notifyStartGame()");
         //   return;
        //}

        // Set playing mode and disable placing/removing of ships
        playingMode = true;
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        buttonPlaceAllShips.setDisable(true);
        buttonRemoveAllShips.setDisable(true);
        buttonReadyToPlay.setDisable(true);
        buttonStartNewGame.setDisable(true);
        buttonPlaceAircraftCarrier.setDisable(true);
        buttonPlaceBattleShip.setDisable(true);
        buttonPlaceCruiser.setDisable(true);
        buttonPlaceSubmarine.setDisable(true);
        buttonPlaceMineSweeper.setDisable(true);
        buttonRemoveShip.setDisable(true);
        showMessage("Start playing by selecting a square in " + opponentName + "\'s grid");
    }

    /**
     * Communicate the result of a shot fired by the player.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALLSUNK - All ships are sunk
     *
     * @param playerNr identification of player
     * @param shotType result of shot fired by player
     */
    @Override
    public void playerFiresShot(int playerNr, ShotType shotType, Point point) {
        SquareState squareState;
        switch (shotType) {
            case HIT:
                squareState = SquareState.SHOTHIT;
                //messageLogger.info("Shot hit one of the ships of " + opponentName);
                break;
            case SUNK:
                squareState = SquareState.SHIPSUNK;
                //messageLogger.info("Ship of " + opponentName + " is sunk.");
                break;
            case ALLSUNK:
                squareState = SquareState.SHIPSUNK;
                //messageLogger.info("All ships of "+ opponentName +  " have sunk.");
                buttonStartNewGame.setDisable(false);
                gameEnded = true;
                break;
            default:
                squareState = SquareState.SHOTMISSED;
                //messageLogger.info("Shot missed all ships of " + opponentName);
                break;
        }
        showSquareOpponent(playerNr, point.getX(), point.getY(), squareState);
    }

    /**
     * Communicate the result of a shot fired by the opponent.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALLSUNK - All ships are sunk
     *
     * @param playerNr identification of player
     * @param shotType result of shot fired by opponent
     */
    @Override
    public void opponentFiresShot(int playerNr, ShotType shotType, Point point) {
        SquareState squareState;
        switch (shotType) {
            case HIT:
                squareState = SquareState.SHOTHIT;
                //messageLogger.info("Shot hit one of the ships of " + playerName);
                break;
            case SUNK:
                squareState = SquareState.SHIPSUNK;
                //messageLogger.info("Ship of " + playerName + " is sunk.");
                break;
            case ALLSUNK:
                squareState = SquareState.SHIPSUNK;
                //messageLogger.info("All ships of "+ playerName +  " have sunk.");
                buttonStartNewGame.setDisable(false);
                gameEnded = true;
                break;
            default:
                squareState = SquareState.SHOTMISSED;
                //messageLogger.info("Shot missed all ships of " + playerName);
                break;
        }
        showSquarePlayer(playerNr, point.getX(), point.getY(), squareState);
    }

    /**
     * Show state of a square in the ocean area.
     * The color of the square depends on the state of the square.
     *
     * @param playerNr    identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param squareState state of square
     */
    @Override
    public void showSquarePlayer(int playerNr, final int posX, final int posY, final SquareState squareState) {
        Platform.runLater(() -> {
            Rectangle square = squaresOceanArea[posX][posY];
            setSquareColor(square, squareState);
        });
    }

    /**
     * Show state of a square in the target area.
     * The color of the square depends on the state of the square.
     *
     * @param playerNr    identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param squareState state of square
     */
    @Override
    public void showSquareOpponent(int playerNr, final int posX, final int posY, final SquareState squareState) {
        Platform.runLater(() -> {
            Rectangle square = squaresTargetArea[posX][posY];
            setSquareColor(square, squareState);
        });
    }

    /**
     * Show error message.
     *
     * @param errorMessage error message
     */
    @Override
    public void showErrorMessage(String errorMessage) {
        showErrorMessage(playerNr, errorMessage);
    }

    /**
     * Show error message.
     *
     * @param playerNr     identification of player
     * @param errorMessage error message
     */
    @Override
    public void showErrorMessage(int playerNr, String errorMessage) {
        showMessage(errorMessage);
    }

    /**
     * Set the color of the square according to position type.
     * Setting the color will be performed by the JavaFX Application Thread.
     *
     * @param square      the square of which the color should be changed.
     * @param squareState position type to determine the color.
     */
    private void setSquareColor(final Rectangle square, final SquareState squareState) {
        // Ensure that changing the color of the square is performed by
        // the JavaFX Application Thread.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (squareState) {
                    case WATER:
                        square.setFill(Color.LIGHTBLUE);
                        break;
                    case SHIP:
                        square.setFill(Color.DARKGRAY);
                        break;
                    case SHOTMISSED:
                        square.setFill(Color.BLUE);
                        break;
                    case SHOTHIT:
                        square.setFill(Color.RED);
                        break;
                    case SHIPSUNK:
                        square.setFill(Color.GREEN);
                        break;
                    default:
                        square.setFill(Color.LIGHTBLUE);
                        break;
                }
            }
        });
    }

    /**
     * Register the player at the game server.
     */
    private void registerPlayer() {
        playerName = textFieldPlayerName.getText();
        if ("".equals(playerName) || playerName == null) {
            showMessage("Enter your name before registering");
            return;
        }
        String playerPassword = passwordFieldPlayerPassword.getText();
        if ("".equals(playerPassword) || playerPassword == null) {
            showMessage("Enter your password before registering");
            return;
        }
        try {
            Client client = new Client("127.0.0.1", 9999, new MessageLogger("CLIENT"));
            client.connect();
            client.ensureConnection();
            game = new SeaBattleGame(this, client);
            game.registerPlayer(playerName, playerPassword, !singlePlayerMode);
        } catch (IOException e) {
            messageLogger.error(e.getMessage());
            showMessage("Connecting with server failed try again later..");
        }
    }

    /**
     * Place the player's ships automatically.
     */
    private void placeShipsAutomatically() {
        // Place the player's ships automatically.
        messageLogger.info(format("Player number is: %s", playerNr));
        game.placeShipsAutomatically(playerNr);
    }

    /**
     * Remove the player's ships.
     */
    private void removeAllShips() {
        // Remove the player's ships
        game.removeAllShips(playerNr);
    }

    /**
     * Notify that the player is ready to start the game.
     */
    private void notifyWhenReady() {
        // Notify that the player is ready is start the game.
        game.notifyWhenReady(playerNr);
    }

    /**
     * Start a new game.
     */
    private void startNewGame() {
        // The player wants to start a new game.
        game.startNewGame(playerNr, !singlePlayerMode);
        playerShips = new ArrayList<>();
        playingMode = false;
        gameEnded = false;
        Platform.runLater(() -> {
            for (Rectangle[] rects : squaresOceanArea) {
                for (Rectangle rect : rects) {
                    rect.setFill(Color.LIGHTBLUE);
                }
            }
            for (Rectangle[] rects : squaresTargetArea) {
                for (Rectangle rect : rects) {
                    rect.setFill(Color.LIGHTBLUE);
                }
            }
        });
    }

    /**
     * Place a ship of a certain ship type. The bow of the ship will
     * be placed at the selected square in the ocean area. The stern is
     * placed to the right of the bow when the ship should be placed
     * horizontally and below of the bow when the ship should be placed
     * vertically. The exact position of the stern depends on the size
     * of the ship.
     *
     * @param shipType   type of the ship to be placed
     * @param horizontal indicates whether ship should be placed horizontally or
     *                   vertically.
     */
    private void placeShipAtSelectedSquare(ShipType shipType, boolean horizontal) {
        if (squareSelectedInOceanArea) {
            int bowX = selectedSquareX;
            int bowY = selectedSquareY;
            game.placeShip(playerNr, shipType, bowX, bowY, horizontal);
        } else {
            showMessage("Select square in " + playerName + "\'s grid to place ship");
        }
    }

    /**
     * Remove ship that is positioned at selected square in ocean area.
     */
    private void removeShipAtSelectedSquare() {
        if (squareSelectedInOceanArea) {
            int posX = selectedSquareX;
            int posY = selectedSquareY;
            game.removeShip(playerNr, posX, posY);
        } else {
            showMessage("Select square in " + playerName + "\'s grid to remove ship");
        }
    }

    /**
     * Show an alert message.
     * The message will disappear when the user presses ok.
     */
    private void showMessage(final String message) {
        // Use Platform.runLater() to ensure that code concerning 
        // the Alert message is executed by the JavaFX Application Thread
        messageLogger.info(format("Show Message for %s - %s", playerName, message));

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sea battle");
            alert.setHeaderText("Message for " + playerName);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Event handler when mouse button is pressed in rectangle in target area.
     * A shot will be fired at the selected square when in playing mode.
     * A message will be shown otherwise.
     *
     * @param event mouse event
     * @param x     x-coordinate of selected square
     * @param y     y-coordinate of selected square
     */
    private void rectangleTargetAreaMousePressed(MouseEvent event, int x, int y) {
        if (playingMode && !gameEnded) {
            // Game is in playing mode
            Rectangle square = squaresTargetArea[x][y];

            if(!square.getFill().equals(Color.LIGHTBLUE)){
                showMessage("You already shot here! Please choose another square.");
                return;
            }

            game.fireShot(playerNr, x, y);
        } else {
            if (gameEnded) {
                showMessage("Press Start new game");
            } else {
                showMessage("Select square in " + playerName + "\'s grid to place ships");
            }
        }
    }

    /**
     * Event handler when mouse button is pressed in rectangle in ocean area.
     * When not in playing mode: the square that was selected before will
     * become light blue and the the selected square will become yellow.
     * A message will be shown when in playing mode.
     *
     * @param event mouse event
     * @param x     x-coordinate of selected square
     * @param y     y-coordinate of selected square
     */
    private void rectangleOceanAreaMousePressed(MouseEvent event, int x, int y) {

        if (!playingMode) {
            // Game is not in playing mode: select square to place a ship
            if (squareSelectedInOceanArea) {
                if (oldColorPress != null) {
                    Rectangle square = squaresOceanArea[selectedSquareX][selectedSquareY];
                    Color color = getPreviousSelectedColor();
                    square.setFill(color);
                }
            }
            selectedSquareX = x;
            selectedSquareY = y;
            oldColorPress = (Color) squaresOceanArea[selectedSquareX][selectedSquareY].getFill();
            squaresOceanArea[x][y].setFill(Color.YELLOW);
            squareSelectedInOceanArea = true;
        } else {
            showMessage("Select square in " + opponentName + "\'s grid to fire");
        }
    }

    private Color getPreviousSelectedColor() {
        Color color = Color.color(oldColorPress.getRed(), oldColorPress.getGreen(), oldColorPress.getBlue());
        Point selectedPoint = new Point(selectedSquareX, selectedSquareY);
        outer:
        for (Ship ship : playerShips) {
            if (ship.isSunk()) {
                color = Color.GREEN;
                break;
            }
            for (Point point : ship.getPoints()) {
                if (point.equals(selectedPoint)) {
                    color = Color.DARKGRAY; // SHIP
                    break outer;
                }
            }
            for (Point point : ship.getPointsHit()) {
                if (point.equals(selectedPoint)) {
                    color = Color.RED; // SHIP
                    break outer;
                }
            }
        }
        return color;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void placeShip(int playerNr, Ship ship) {
        playerShips.add(ship);
        redrawShips();
    }

    @Override
    public void removeShip(int playerNumber, Ship shipToRemove) {
        Stream<Ship> shipStream = playerShips.stream();
        final Optional<Ship> optionalShip = shipStream.filter(x -> x.getClass().equals(shipToRemove.getClass())).findFirst();
        if (optionalShip.isPresent()) {
            playerShips.remove(optionalShip.get());
        } else {
            messageLogger.error("removeShip failed!");
        }
        redrawShips();
        oldColorPress = null;
    }

    private void redrawShips() {
        Platform.runLater(() -> {
            for (Rectangle[] rects : squaresOceanArea) {
                for (Rectangle rect : rects) {
                    rect.setFill(Color.LIGHTBLUE);
                }
            }
            for (Ship ship : playerShips) {
                if (ship.isSunk()) {
                    for (Point point : ship.getPointsHit()) {
                        Rectangle square = squaresOceanArea[point.getX()][point.getY()];
                        setSquareColor(square, SquareState.SHIPSUNK);
                    }
                } else {
                    for (Point point : ship.getPoints()) {
                        Rectangle square = squaresOceanArea[point.getX()][point.getY()];
                        setSquareColor(square, SquareState.SHIP);
                    }
                    for (Point point : ship.getPointsHit()) {
                        Rectangle square = squaresOceanArea[point.getX()][point.getY()];
                        setSquareColor(square, SquareState.SHOTHIT);
                    }
                }
            }
        });
    }

    @Override
    public void resetGUI() {
        Platform.runLater(() -> start(primaryStage));
    }
}
