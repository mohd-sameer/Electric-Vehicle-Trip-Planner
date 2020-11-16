package com.example.seccharge;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TpContext implements Serializable {
    private List<googleDirection> data = new ArrayList<googleDirection>();
    private List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private double distanceCovered;
    private LatLng source;
    private double totalDistance;
    private List<PolylineOptions> polylines = new ArrayList<PolylineOptions>();
    private List<LatLng> polylinepoints = new ArrayList<LatLng>();


    public TpContext(LatLng source, LatLng destination, List<googleDirection> data, List<MarkerOptions> markers, List<PolylineOptions> polylines, double distanceCovered, double totalDistance) {
        this.data = data;
        this.distanceCovered = distanceCovered;
        this.markers = markers;
        this.source = source;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.polylines = polylines;

    }


    public double getdistanceCovered() {
        return distanceCovered;
    }

    public void setdistanceCovered(double distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public List<MarkerOptions> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerOptions> markers) {
        this.markers = markers;
    }

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    private LatLng destination;

    public List<googleDirection> getData() {
        return data;
    }

    public void setData(List<googleDirection> data) {
        this.data = data;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<PolylineOptions> getPolylines() {
        return polylines;
    }

    public void setPolylines(List<PolylineOptions> polylines) {
        this.polylines = polylines;
    }

    public List<LatLng> getPolylinepoints() {
        return polylinepoints;
    }

    public void setPolylinepoints(List<LatLng> polylinepoints) {
        this.polylinepoints = polylinepoints;
    }

}
