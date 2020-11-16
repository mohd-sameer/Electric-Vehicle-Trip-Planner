package com.example.seccharge;

public class Tripsetup {
    private boolean lastReserv;
    private Long tripId;
    private Long resId;
    private double criticalDistance;
    private boolean success;

    public Tripsetup(Long tripId, Long resId, double criticalDistance, boolean success,
                     boolean lastReserv) {
        this.lastReserv = lastReserv;
        this.tripId = tripId;
        this.resId = resId;
        this.criticalDistance = criticalDistance;
        this.success = success;
    }


    public boolean isLastReserv() {
        return lastReserv;
    }

    public void setLastReserv(boolean lastReserv) {
        this.lastReserv = lastReserv;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public double getCriticalDistance() {
        return criticalDistance;
    }

    public void setCriticalDistance(double criticalDistance) {
        this.criticalDistance = criticalDistance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

