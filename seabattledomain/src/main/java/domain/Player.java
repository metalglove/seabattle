package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class Player {
    private final String username;
    private final String password;
    private final Integer number;
    private List<Ship> ships = new CopyOnWriteArrayList<>();
    private boolean gamehasStarted = false;

    public Player(String username, String password, Integer number) {

        this.username = username;
        this.password = password;
        this.number = number;
    }

    public boolean addShip(Ship ship) {
        if (gamehasStarted)
            return false;

        Stream<Ship> shipStream = ships.stream().filter(x -> x.getClass().equals(ship.getClass()));
        final Optional<Ship> optionalShip = shipStream.findFirst();
        if (optionalShip.isPresent()) {
            Ship oldShip = optionalShip.get();
            ships.remove(oldShip);
        }

        ships.add(ship);
        return true;
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
}
