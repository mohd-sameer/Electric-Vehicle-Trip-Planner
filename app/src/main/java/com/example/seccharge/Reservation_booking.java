package com.example.seccharge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.seccharge.Login.LoginActivity;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static java.lang.Double.parseDouble;


public class Reservation_booking extends AppCompatActivity {
    ArrayList<String> productList;
    RecyclerView timeSlotRecyclerView;
    Button proceed;
    TextView goback;

    RequestQueue queue;
    boolean firstReservationCall;
    Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_booking);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,Calendar.DATE);
        proceed=findViewById(R.id.proceed);
        goback=findViewById(R.id.goback);
        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        queue = Volley.newRequestQueue(this);  // this = context

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });


        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Reservation_booking.this, navigation_drawer.class);
                startActivity(i);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean inTrip = TripEnhanced.inTrip;
                Log.d("inTrip value", String.valueOf(inTrip));
                Log.d("reservationId", String.valueOf(TripEnhanced.reservationId));
                if(inTrip) {
                    if(TripEnhanced.reservationId == 0){
                        JSONObject header = new JSONObject();
                        String cmd = getString(R.string.url) + "api/reservation";
                        Log.d("Proceed Endpoint", cmd);
                        // Header to reservations are added once the API is available
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cmd,
                                header, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    TripEnhanced.reservationId = response.getLong("reservationId");
                                    tripPlannerApi();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(Reservation_booking.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Reservation_booking.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                        queue.add(request);
                    }
                    else{
                        tripPlannerApi();
                    }
                } else{
                    i = new Intent(Reservation_booking.this,SubmitCreditCardActivity.class);
                    startActivity(i);
                }

            }
        });



       /* timeSlotRecyclerView = (RecyclerView) findViewById(R.id.time_slot_recyclerview);
        timeSlotRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadProducts();*/

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new NumberedAdapter(9,17));
    }

    private void tripPlannerApi(){
        Log.d("API","waiting");
        JsonObjectRequest request;
        String cmd = getString(R.string.url) + "api/tripPlannerReservation";
        JSONObject header = new JSONObject();
        if (TripEnhanced.tripId == 0) {
            try {
                header.put("reservationId", TripEnhanced.reservationId);
                header.put("lastReservation", TripEnhanced.lastReservation);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request = new JsonObjectRequest(Request.Method.GET, cmd,
                    header, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        TripEnhanced.tripId = response.getLong("tripId");
                        TripEnhanced.currentDistance += response.getDouble("criticalDistance");
                        navigation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Reservation_booking.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            try {
                header.put("reservationId", TripEnhanced.reservationId);
                header.put("tripId", TripEnhanced.tripId);
                header.put("lastReservation", TripEnhanced.lastReservation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            request = new JsonObjectRequest(Request.Method.GET, cmd,
                    header, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        TripEnhanced.currentDistance += response.getDouble("criticalDistance");
                        navigation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Reservation_booking.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }
        queue.add(request);
    }

    private void navigation(){
        Intent i = new Intent(Reservation_booking.this,navigation_drawer.class);
        startActivity(i);
    }

    private void loadProducts() {
        productList.add("6");
        productList.add("7");
        productList.add("8");
        productList.add("6");
        productList.add("7");
        productList.add("8");
        productList.add("6");
        productList.add("7");
        productList.add("8");

        final TimeSlotAdapter adapter = new TimeSlotAdapter(Reservation_booking.this, productList);
        timeSlotRecyclerView.setAdapter(adapter);
    }

}
