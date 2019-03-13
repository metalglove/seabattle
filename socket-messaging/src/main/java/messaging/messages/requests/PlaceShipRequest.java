package messaging.messages.requests;

import domain.ShipType;
import messaging.messages.Message;

public class PlaceShipRequest  extends Message {
    public final int playerNr;
    public final ShipType shipType;
    public final int bowX;
    public final int bowY;
    public final boolean horizontal;

    public PlaceShipRequest(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        this.playerNr = playerNr;
        this.shipType = shipType;
        this.bowX = bowX;
        this.bowY = bowY;
        this.horizontal = horizontal;
    }
}