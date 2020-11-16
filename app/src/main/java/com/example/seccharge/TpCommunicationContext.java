package com.example.seccharge;

import java.io.Serializable;

public class TpCommunicationContext implements Serializable {
    private boolean firstReservationCall;
    private boolean inTrip;
    private boolean firstTripCall;

    public boolean isInTrip() {
        return inTrip;
    }

    public void setInTrip(boolean inTrip) {
        this.inTrip = inTrip;
    }


}
