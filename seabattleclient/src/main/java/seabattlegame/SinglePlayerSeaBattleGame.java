package seabattlegame;

import domain.ShipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seabattlegui.ISeaBattleGUI;

public class SinglePlayerSeaBattleGame implements ISeaBattleGame {

    private static final Logger log = LoggerFactory.getLogger(SinglePlayerSeaBattleGame.class);
    private ISeaBattleGUI application;

    public SinglePlayerSeaBattleGame(ISeaBattleGUI application) {
        this.application = application;
    }

    @Override
    public void registerPlayer(String name, String password) {

    }

    @Override
    public void placeShipsAutomatically(int playerNr) {

    }

    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {

    }

    @Override
    public void removeShip(int playerNr, int posX, int posY) {

    }

    @Override
    public void removeAllShips(int playerNr) {

    }

    @Override
    public void notifyWhenReady(int playerNr) {

    }

    @Override
    public void fireShot(int playerNr, int posX, int posY) {

    }

    @Override
    public void startNewGame(int playerNr) {

    }
}
