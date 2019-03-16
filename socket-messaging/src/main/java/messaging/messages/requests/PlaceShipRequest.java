package messaging.messages.requests;

import domain.ShipType;
import messaging.messages.Message;

public class PlaceShipRequest extends Message {
    public final int playerNumber;
    public final ShipType shipType;
    public final int bowX;
    public final int bowY;
    public final boolean horizontal;

    public PlaceShipRequest(int playerNumber, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        this.playerNumber = playerNumber;
        this.shipType = shipType;
        this.bowX = bowX;
        this.bowY = bowY;
        this.horizontal = horizontal;
    }
}