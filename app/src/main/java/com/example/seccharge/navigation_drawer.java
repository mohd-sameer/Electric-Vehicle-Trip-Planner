package com.example.seccharge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.seccharge.Dashboard.Dashboard;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Double.isNaN;
import static java.lang.Double.parseDouble;

public class navigation_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    public GoogleApiClient mApiClient;
    List<String> testing;
    AutocompleteSupportFragment toautocompleteFragment, autocompleteFragment;

    public GoogleMap mMap;
    private Map<LatLng, String> markerData = new HashMap<LatLng, String>(); // get all data for markers
    public Map<String,String> charger_data = new HashMap<String,String>();
    public Map<String,ArrayList> connector_hash_data= new HashMap<String, ArrayList>();
    TextView stationName; //station name

    private CameraPosition mCameraPosition;
    String apiKey;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    SupportMapFragment mapFragment;
    Button reserve;
    RequestQueue queue;
    Button filter, tripplanner, routesearch;
    SeekBar seek_bar;
    TextView radius;
    Spinner charger_type,connector_type;
    Double onplace_selected_latitude;
    Double onplace_selected_logitude;
    private String selected_charger_type;
    private String selected_connector_type;
    private  int seek_progress_value = 10;
    HashMap<String,String> connector_name_to_id;
    LinearLayout connector_type_layout;
    Marker destMark = null;
    Marker originMark = null;
    Marker criticalMark = null;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;


    BottomSheetBehavior sheetBehavior;
    LatLngBounds.Builder builder;

    View maps;
    Authinfo userDetail;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiKey = "Keep your  key here";


        maps = findViewById(R.id.mapcomplete);
        builder = new LatLngBounds.Builder();
        queue = Volley.newRequestQueue(this);  // this = context

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        userDetail = Savesharedpreference.getUserDetails(navigation_drawer.this);

        connector_name_to_id = new HashMap<String, String>();
        navigationView.setNavigationItemSelectedListener(this);
        filter = findViewById(R.id.filter);
        all_maps(savedInstanceState);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(navigation_drawer.this);
                View mView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                seek_bar = (SeekBar) mView.findViewById(R.id.seekBar);
                radius = (TextView) mView.findViewById(R.id.radius);
                charger_type = (Spinner) mView.findViewById(R.id.charger_type);
                connector_type = (Spinner) mView.findViewById(R.id.connector_type);
                connector_type_layout=mView.findViewById(R.id.connector_type_layout);
                connector_type_layout.setVisibility(View.INVISIBLE);

                seebbarr();
                setcharger_type();

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!charger_type.getSelectedItem().toString().equalsIgnoreCase("choose")&&!connector_type.getSelectedItem().toString().isEmpty()) {
                            markpoints(mMap, onplace_selected_latitude, onplace_selected_logitude, seek_progress_value, charger_data.get(charger_type.getSelectedItem()), connector_name_to_id.get(connector_type.getSelectedItem()));
                            //   Toast.makeText(getBaseContext(), charger_type.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                        else
                        {
                            markpoints(mMap, onplace_selected_latitude, onplace_selected_logitude, seek_progress_value,"0", "0");
                            //   Toast.makeText(getBaseContext(), charger_type.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    }
                });
            }
        });

        tripplanner = findViewById(R.id.tripplanner);
        tripplanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripSearchFields();
            }
        });


        // Trip planner button click
        routesearch = findViewById(R.id.routeSearch);
        routesearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(TripEnhanced.dest!=null && TripEnhanced.origin!=null) {
                    MarkerOptions mar = new MarkerOptions();
                    mMap.addMarker(mar.position(TripEnhanced.dest).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    builder.include(mar.getPosition());

                    mMap.addMarker(mar.position(TripEnhanced.origin).title("Origin").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    builder.include(mar.getPosition());

                    LatLngBounds bounds = builder.build();
                    int padding = 0;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                    mMap.animateCamera(cu);
                    tripRoute();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Both source and destination needs " +
                            "to selected for trip planner", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowFragment(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_account:
                fragment = new profile();
                break;
            case R.id.nav_dashboard:
                fragment = new Dashboard();
                break;
            case R.id.nav_vehicle:
                fragment = new Vehicles();
                break;
            case R.id.nav_home:
                maps.setVisibility(View.VISIBLE);
                break;

            case R.id.nav_payment:
                Intent i = new Intent(this, Payment.class);
                startActivity(i);
                break;
        }

        if (fragment != null) {
            maps.setVisibility(View.INVISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ShowFragment(item.getItemId());
        return true;
    }

    public void all_maps(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        reserve = findViewById(R.id.reserve);
        stationName = findViewById(R.id.station_name);


        // bottom sheet listener

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@android.support.annotation.NonNull @NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }

                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@android.support.annotation.NonNull @NonNull View bottomSheet, float slideOffset) {

            }
        });

        //charger type to connector type
        //reserve button in butter knife view
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(navigation_drawer.this, Reservation_booking.class);
                startActivity(i);
                //  mMap.clear();
            }
        });


        //check for gps and get location permission
        checkforgps();
        getLocationPermission();

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
                   }


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get my current location button
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        View mapView = mapFragment.getView();
        assert mapView != null;
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();// position on right bottom

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);


        //places api setup
        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey);
        Places.initialize(getApplicationContext(), "keep your key here");

        // Create a new Places client instance.
        // PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
         autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@android.support.annotation.NonNull Place place) {
                TripEnhanced.origin = place.getLatLng();
                onplace_selected_latitude=place.getLatLng().latitude;
                onplace_selected_logitude=place.getLatLng().longitude;
                if(!TripEnhanced.inTrip) {
                    markpoints(mMap, place.getLatLng().latitude, place.getLatLng().longitude, 100, "0", "0");
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
                }
            }

            @Override
            public void onError(@android.support.annotation.NonNull Status status) {

            }
        });

        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TripEnhanced.origin = null;
                        clearTpData();
                        autocompleteFragment.setText("");
                    }
                });


        toautocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.toautocomplete);
        toautocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        // Set up a PlaceSelectionListener to handle the response.


        toautocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TripEnhanced.dest = null;
                        clearTpData();
                        toautocompleteFragment.setText("");
                    }
                });


        toautocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@android.support.annotation.NonNull Place place) {
                TripEnhanced.dest = place.getLatLng();
            }
            @Override
            public void onError(@android.support.annotation.NonNull Status status) {

            }
        });

    }

    //called when is map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        if (TripEnhanced.inTrip){
        try {
            setTripPlanner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }else {
            getDeviceLocation();
        }
    }

    private void markpoints(GoogleMap googleMap, double latitude, double longitude, int radius,String charger_type,String connector_type) {
        Log.d("hm", "markpoints: ");
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker); // set marker icon
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
        final String authEndPoint = getString(R.string.url) + "api/chargingSites/locationWise?radius="+radius+"&latitude=" + latitude + "&longitude=" + longitude + "&chargerType="+charger_type+"&connectorType="+connector_type;
        Log.d("hm", "markpoints: " + authEndPoint);
        final JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, authEndPoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
                        try {
                            //Parse the JSON response array by iterating over it responseArray;
                            Toast.makeText(getApplicationContext(), authEndPoint, Toast.LENGTH_LONG).show();
                            for (int i = 0; i <= responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String longLocation = response.getString("longLocation");
                                String latLocation = response.getString("latLocation");
                                String stationName = response.getString("siteOwner");
                                Double lat1 = parseDouble(latLocation);
                                Double long1 = parseDouble(longLocation);
                                LatLng latLng = new LatLng(lat1, long1);

                                //set options for the marker
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                Log.d("hm", "markpointsinside the call: ");
                                mMap.addMarker(markerOptions); //add marker in the map
                                markerData.put(latLng, stationName); //store data in hashmap with lat/long as key
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                            }
                        }
                );
        // add it to the RequestQueue
        queue.add(jsArrayRequest);

        // marker on click listener
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                boolean userMarker = false;

                /*
                stationName.setText(markerData.get(marker.getPosition()));
                // Need to control onclick of marker
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return false;
                */

                ArrayList<String> arr = new ArrayList<String>(3);
                arr.add("Critical Point");
                arr.add("Destination");
                arr.add("Origin");

                if(arr.contains(marker.getTitle()))
                {
                    userMarker = true;
                }

                if(!userMarker) {
                    stationName.setText(markerData.get(marker.getPosition()));
                    // Need to control onclick of marker
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return false;
                }
                return true;


            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                //mMap.getUiSettings().setMyLocationButtonEnabled(false);

                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {

                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            onplace_selected_latitude=mLastKnownLocation.getLatitude();
                            onplace_selected_logitude=mLastKnownLocation.getLongitude();
                            markpoints(mMap,mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),100,"0","0");
                       //     Toast.makeText(getBaseContext(), String.valueOf(mLastKnownLocation.getLatitude()),Toast.LENGTH_SHORT).show();

                        } else {

                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            // mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Method that prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Method to check whether the location services(GPS)
     * on the user's mobile is turned on
     */
    public void checkforgps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void setConnector_type(String s){
        connector_name_to_id.put("Choose","0");
        connector_type_layout.setVisibility(View.VISIBLE);
        final List<String> connector_types = new ArrayList<String>();
        ArrayList temp = connector_hash_data.get(s);
        for(int k=0;k<temp.size();k++) {
            ConnectorType array = (ConnectorType) temp.get(k);
            String a = array.getConnector_type();
            connector_name_to_id.put(a,array.getId());
          //  Toast.makeText(getBaseContext(),connector_name_to_id.get(a),Toast.LENGTH_SHORT).show();
            connector_types.add(a);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, connector_types);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        connector_type.setAdapter(dataAdapter);
    }


    public void setcharger_type() {
        final List<String> charger_types = new ArrayList<String>();
        charger_data.put("Choose","0");
        final String authEndPoint = getString(R.string.url) + "api/chargerTypes";
        final JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, authEndPoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        try {
                            //Parse the JSON response array by iterating over it responseArray;
                            //Toast.makeText(getApplicationContext(), authEndPoint, Toast.LENGTH_LONG).show();
                            for (int i = 0; i <= responseArray.length(); i++) {
                               // Log.d("here"+i, "come ");

                                JSONObject response = responseArray.getJSONObject(i);
                                String type = response.getString("type");
                                String energylevel = response.getString("energyLevel");
                                String id = response.getString("id");
                                JSONArray connector = response.getJSONArray("connectorTypes");
                                ArrayList<ConnectorType> connectorTypesdata = new ArrayList<ConnectorType>();
                                    for(int j=0;j<connector.length();j++) {
                                        JSONObject connector_object = connector.getJSONObject(j);

                                        String connector_id = connector_object.getString("id");
                                        String connector_type = connector_object.getString("type");
                                        ConnectorType  obj = new ConnectorType(connector_id,connector_type);
                                        connectorTypesdata.add(obj);
                                     //   Log.d("kumar", "onResponse: ");
                                     //
                                        //Toast.makeText(getBaseContext(), connector_type, Toast.LENGTH_SHORT).show();


                                    }

                                connector_hash_data.put(id, connectorTypesdata);
                                 /*   ArrayList temp = connector_hash_data.get(id);
                                    for(int k=0;k<temp.size();k++) {
                                        ConnectorType array = (ConnectorType) temp.get(k);
                                        String a = array.getId();
                                        Log.d("kum", a);
                                    }*/

                                charger_types.add(type+"-"+energylevel);
                                charger_data.put(type+"-"+energylevel,id);
                              //  Toast.makeText(getBaseContext(),charger_data.get(type+"-"+energylevel),Toast.LENGTH_LONG).show();
                                selected_charger_type=charger_data.get(type+"-"+energylevel);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                            }
                        }
                );
        // add it to the RequestQueue
        queue.add(jsArrayRequest);
        charger_types.add("Choose");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, charger_types);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        charger_type.setAdapter(dataAdapter);
        charger_type.setOnItemSelectedListener(this);
    }

    public void seebbarr() {
        radius.setText("Radius : " + seek_bar.getProgress() + " km ");
        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int MIN = 10;
                        if (progress < MIN) {
                            seek_progress_value = 10;
                           // Toast.makeText(navigation_drawer.this, "Min radius is 10", Toast.LENGTH_LONG).show();
                            radius.setText("Radius : " + 10 + " / " + seek_bar.getMax());

                        } else {
                           seek_progress_value = progress;
                            radius.setText("Radius : " + progress + " / " + seek_bar.getMax());
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                       // radius.setText("Radius : " + progress_value + " / " + seek_bar.getMax());
                      //  Toast.makeText(navigation_drawer.this, "Selected Radius"+seek_progress_value, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(!adapterView.getSelectedItem().toString().equalsIgnoreCase("choose")) {
            setConnector_type(charger_data.get(adapterView.getSelectedItem().toString()));
          //  Toast.makeText(getBaseContext(), charger_data.get(adapterView.getSelectedItem().toString()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    /*
            TRIP PLANNER CODE - ENHANCED VERSION WITH PROPER ROUTES
     */

    public void tripSearchFields(){
        // The From and To fields are generated dynamically
        CardView tocardview = findViewById(R.id.to);
        autocompleteFragment.setText("");
        toautocompleteFragment.setText("");
        if (TripEnhanced.inTrip) {
            tocardview.setVisibility(View.INVISIBLE);
            autocompleteFragment.setHint("Search");
            TripEnhanced.inTrip = false;
            clearTpData();
        } else {
            autocompleteFragment.setHint("From Location");
            toautocompleteFragment.setHint("To Location");
            tocardview.setVisibility(View.VISIBLE);
            TripEnhanced.inTrip = true;
        }
    }


    public String address(LatLng temp) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(temp.latitude, temp.longitude, 1);
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        // String postalCode = addresses.get(0).getPostalCode();
        // String knownName = addresses.get(0).getFeatureName();
        return address+","+city+","+state+","+country;
    }

    public void tripRoute() {
        // Google Direction API
        String googlekey = "key=" + apiKey;
        String orgn = "origin=" + TripEnhanced.origin.latitude + "," + TripEnhanced.origin.longitude;
        String des = "destination=" + TripEnhanced.dest.latitude + "," + TripEnhanced.dest.longitude;
        String sensor = "sensor=false";
        String parameters = googlekey + "&" + orgn + "&" + des + "&" + sensor;
        String output = "json";
        String URL = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("url", "getDirectionsUrl: "+URL);

        // Getting critical point
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        drawRoute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "hm");
                    }
                }
        );
        queue.add(getRequest);
    }

    private void drawRoute(JSONObject resp) {
        try {
            JSONArray routeObject = resp.getJSONArray("routes");
            JSONObject routes = routeObject.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String points = overviewPolylines.getString("points");
            TripEnhanced.lineLatLng = PolyUtil.decode(points);
            mMap.addPolyline(new PolylineOptions().addAll(TripEnhanced.lineLatLng).color(Color.GREEN));
            intialCriticalPoint();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void intialCriticalPoint() throws JSONException {
        String URL = getString(R.string.url)+"/api/criticalDistance";
        JSONObject params = new JSONObject();
        params.put("userId",userDetail.getUserId());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TripEnhanced.currentDistance += response.getDouble("criticalDistance");
                            criticalPoint();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "hm" + error);
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    private void criticalPoint(){
        double distance = TripEnhanced.currentDistance;
        double total = 0;
        MarkerOptions mar = new MarkerOptions();
        LatLng newLatLang = null;
        for(int i=0; i<TripEnhanced.lineLatLng.size()-1;i++) {
            float[] result = new float[1];
            Location.distanceBetween(TripEnhanced.lineLatLng.get(i).latitude,TripEnhanced.lineLatLng.get(i).longitude,
                TripEnhanced.lineLatLng.get(i+1).latitude,TripEnhanced.lineLatLng.get(i+1).longitude, result);
            total+= result[0]/1000;  // mts to kms
            if(total == distance){
                mMap.addMarker(mar.position(TripEnhanced.lineLatLng.get(i+1)).title("Critical Point"));
                TripEnhanced.criticalMarkersOptions.add(mar);
                break;
            }else if (total > distance){
                // Finding Bearing Angle
                double angle = CalculateBearingAngle(TripEnhanced.lineLatLng.get(i).latitude,
                        TripEnhanced.lineLatLng.get(i).longitude,TripEnhanced.lineLatLng.get(i+1).latitude,
                        TripEnhanced.lineLatLng.get(i+1).longitude);
                newLatLang = movePoint(TripEnhanced.lineLatLng.get(i).latitude,
                        TripEnhanced.lineLatLng.get(i).longitude, total-distance,angle);
                mMap.addMarker(mar.position(newLatLang).title("Critical Point"));
                TripEnhanced.criticalMarkersOptions.add(mar);
                break;
            }
        }
        if(newLatLang!=null){
            markpoints(mMap, newLatLang.latitude, newLatLang.longitude, seek_progress_value,"0", "0");
        }else{
            lastCriticalPoint();
        }
    }

    private void lastCriticalPoint(){
        Toast.makeText(navigation_drawer.this, "All the reservations along the path are completed." +
                "Redirecting to payment for card details", Toast.LENGTH_SHORT).show();
        JSONObject header = new JSONObject();
        String cmd = getString(R.string.url) + "api/tripPlannerReservation";
        try {
            header.put("reservationId", TripEnhanced.reservationId);
            header.put("tripId", TripEnhanced.tripId);
            header.put("lastReservation", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cmd,
                header, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent i = new Intent(navigation_drawer.this,SubmitCreditCardActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
    }


    private double CalculateBearingAngle(double startLatitude,double startLongitude, double endLatitude, double endLongitude){
        double start = Math.toRadians(startLatitude);
        double end = Math.toRadians(endLatitude);
        double DeltaLambda = Math.toRadians(endLongitude - startLongitude);
        double Theta = Math.atan2((Math.sin(DeltaLambda)*Math.cos(end)) , (Math.cos(start)*Math.sin(end) - Math.sin(start)*Math.cos(end)*Math.cos(DeltaLambda)));
        return (double)Math.toDegrees(Theta);
    }

    private LatLng movePoint(double latitude, double longitude, double distanceInMetres, double bearing) {
        double brngRad = Math.toRadians(bearing);
        double latRad = Math.toRadians(latitude);
        double lonRad = Math.toRadians(longitude);
        int earthRadiusInMetres = 6371000;
        double distFrac = distanceInMetres / earthRadiusInMetres;
        double latitudeResult = Math.asin(Math.sin(latRad) * Math.cos(distFrac) + Math.cos(latRad) * Math.sin(distFrac) * Math.cos(brngRad));
        double a = Math.atan2(Math.sin(brngRad) * Math.sin(distFrac) * Math.cos(latRad), Math.cos(distFrac) - Math.sin(latRad) * Math.sin(latitudeResult));
        double longitudeResult = (lonRad + a + 3 * Math.PI) % (2 * Math.PI) - Math.PI;
        return new LatLng(Math.toDegrees(latitudeResult), Math.toDegrees(longitudeResult));
    }

    private void setTripPlanner() throws IOException {
        mMap.clear();
        CardView tocardview = findViewById(R.id.to);
        tocardview.setVisibility(View.VISIBLE);
        mMap.addPolyline(new PolylineOptions().addAll(TripEnhanced.lineLatLng).color(Color.GREEN));
        autocompleteFragment.setText(address(TripEnhanced.origin));
        toautocompleteFragment.setText(address(TripEnhanced.dest));
        criticalPoint();
        for (MarkerOptions marker : TripEnhanced.criticalMarkersOptions) {
            mMap.addMarker(marker);
        }
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                MarkerOptions mar = new MarkerOptions();
                mMap.addMarker(mar.position(TripEnhanced.dest).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                builder.include(mar.getPosition());
                mMap.addMarker(mar.position(TripEnhanced.origin).title("Origin").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                builder.include(mar.getPosition());
                LatLngBounds bounds = builder.build();
                int padding = 0;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.moveCamera(cu);
                mMap.animateCamera(cu);
            }
        });
    }

    private void clearTpData(){
        mMap.clear();
        TripEnhanced.clearData();
    }
}
