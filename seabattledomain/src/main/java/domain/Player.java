package domain;

import domain.actions.AddShipAction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class Player {
    private final String username;
    private final Integer number;
    private List<Ship> ships = new CopyOnWriteArrayList<>();
    private boolean gameHasStarted = false;
    private boolean _isReady = false;

    public Player(String username, Integer number) {

        this.username = username;
        this.number = number;
    }

    public AddShipAction addShip(Ship ship) {
        AddShipAction addShipAction = new AddShipAction( null, false, false);
        if (gameHasStarted || !ship.isWithinBounds())
            return addShipAction;

        Stream<Ship> shipStream = ships.stream();
        Ship oldShip = null;
        final Optional<Ship> optionalShip = shipStream.filter(x -> x.getClass().equals(ship.getClass())).findFirst();
        if (optionalShip.isPresent()) {
            oldShip = optionalShip.get();
            ships.remove(oldShip);
        }
        for (Ship shipInList : ships) {
            for (Point point : ship.getPoints())
                if (shipInList.containsPoint(point)) {
                    if (oldShip != null) {
                        ships.add(oldShip);
                    }
                    return addShipAction;
                }
        }
        ships.add(ship);
        boolean hasPlacedAllShips = ships.size() == 5;
        return new AddShipAction(oldShip, hasPlacedAllShips, true);
    }

    public String getUsername() {
        return username;
    }

    public Integer getPlayerNumber() {
        return number;
    }

    @Override
    public boolean equals(Object username) {
        if (!(username instanceof String)) return false;
        return this.username.equals(username);
    }

    public List<Ship> getShips() {
        return List.copyOf(ships);
    }

    public void removeShip(Ship shipToRemoveIfNeeded) {
        ships.remove(shipToRemoveIfNeeded);
    }

    public void setReady() {
        _isReady = true;
    }

    public void setUnReady() {
        _isReady = false;
    }

    public boolean isReady() {
        return _isReady;
    }
}
