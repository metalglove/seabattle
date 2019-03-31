package domain;

import dtos.PlaceShipResultDto;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class Player {
    private final String username;
    private final String password;
    private final Integer number;
    private List<Ship> ships = new CopyOnWriteArrayList<>();
    private boolean gameHasStarted = false;
    private boolean _isReady = false;

    public Player(String username, String password, Integer number) {

        this.username = username;
        this.password = password;
        this.number = number;
    }

    public PlaceShipResultDto addShip(Ship ship) {
        PlaceShipResultDto placeShipResultDto = new PlaceShipResultDto(null, null, false, false);
        if (gameHasStarted || !ship.isWithinBounds())
            return placeShipResultDto;

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
                    return placeShipResultDto;
                }
        }
        ships.add(ship);
        boolean hasPlacedAllShips = ships.size() == 5;
        return new PlaceShipResultDto(ship, oldShip, hasPlacedAllShips, true);
    }

    public String getUsername() {
        return username;
    }

    public Integer getPlayerNumber() {
        return number;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
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
