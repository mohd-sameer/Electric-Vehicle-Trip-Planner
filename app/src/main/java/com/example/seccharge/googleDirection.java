package com.example.seccharge;

import com.google.android.gms.maps.model.LatLng;

public class googleDirection {
    private LatLng startLocation;
    private LatLng endLocation;
    private Double distance;

    public googleDirection(LatLng startLocation, LatLng endLocation, Double distance) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
