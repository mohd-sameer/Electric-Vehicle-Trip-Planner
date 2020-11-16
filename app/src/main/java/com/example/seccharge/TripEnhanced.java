package com.example.seccharge;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class TripEnhanced {
    public static long reservationId, tripId = 0;
    public static boolean lastReservation, inTrip = false;
    public static LatLng origin, dest;
    public static double currentDistance, totalDistance, criticalDistance = 0;
    public static List<LatLng> lineLatLng =new ArrayList<LatLng>();
    public static List<MarkerOptions> criticalMarkersOptions = new ArrayList<MarkerOptions>();

    public static void clearData(){
        TripEnhanced.reservationId = 0;
        TripEnhanced.tripId = 0;
        TripEnhanced.lastReservation = false;
        TripEnhanced.inTrip = false;
        TripEnhanced.origin = null;
        TripEnhanced.dest = null;
        TripEnhanced.currentDistance = 0;
        TripEnhanced.totalDistance = 0;
        TripEnhanced.criticalDistance = 0;
        TripEnhanced.lineLatLng.clear();
        TripEnhanced.criticalMarkersOptions.clear();
    }
}
