package com.example.seccharge;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class Vehicles extends Fragment {
    AlertDialog.Builder builder;
    List<Vehicle> list;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView ;
    EditText vehicleVin, plateNumber, make, model, year, vehicleType, vehicleColor;
    TextInputLayout textInputvehicleVin, textInputplateNumber, textInputmake, textInputmodel,
            textInputyear, textInputvehicleType;
    boolean validvehicleVin, validplateNumber, validInputmake, validmodel,
            validyear, validvehicleType = false;
    boolean value = false;
    CheckBox primaryVehicle;
    RequestQueue queue;
    Authinfo userDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_vehicles, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        Objects.requireNonNull(getActivity()).setTitle("Vehicles");
        list = new ArrayList<>();
        setListview();
        vehiclesDetail();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());  // this = context
        userDetail = Savesharedpreference.getUserDetails(getActivity().getApplicationContext());

    }

    private void setListview(){
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recyclerviewid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new com.example.seccharge.RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new com.example.seccharge.ItemClickListener() {
            @Override
            public void OnItemClick(int position, com.example.seccharge.Vehicle vehData) {
                builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setTitle("Update Vehicle Info");
                modifyInputDialog(position);
                builder.setCancelable(false);
            }
        });
    }

    private void vehiclesDetail() {
        String JSON_URL = getString(R.string.url)+"api/electricalVehicles";
        JsonArrayRequest request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        com.example.seccharge.Vehicle vehicle = new com.example.seccharge.Vehicle(
                                jsonObject.getLong("id"),
                                jsonObject.getString("vehicleVin"),
                                jsonObject.getString("plateNumber"),
                                jsonObject.getString("make"),
                                jsonObject.getString("model"),
                                jsonObject.getString("year"),
                                jsonObject.getString("vehicleType"),
                                jsonObject.getString("vehicleColor"),
                                jsonObject.getBoolean("primaryVehicle"),
                                jsonObject.getLong("userId")
                        );
                        list.add(vehicle);
                    } catch (JSONException e) {
                        e.printStackTrace(); }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", String.valueOf(error));
            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        requestQueue.add(request) ;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vehicles, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_vehicle:
                saveInputDialog();
                break;

            case R.id.action_settings:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    protected void saveInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.vehicle_details, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setTitle("Save Vehicle Info");
        alertDialogBuilder.setView(promptView);
        vehicleVin = promptView.findViewById(R.id.vehicleVin);
        plateNumber = promptView.findViewById(R.id.plateNumber);
        make = promptView.findViewById(R.id.make);
        model = promptView.findViewById(R.id.model);
        year = promptView.findViewById(R.id.year);
        vehicleType = promptView.findViewById(R.id.vehicleType);
        vehicleColor = promptView.findViewById(R.id.vehicleColor);
        primaryVehicle = promptView.findViewById(R.id.primaryVehicle);
        if(primaryVehicle.isChecked())
            value = true;
        // setup a dialog window

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            addVehicle();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        actionListeners(promptView);
    }

    private void addVehicle() throws JSONException {
        final String endpoint = getString(R.string.url) + "api/electricalVehicles";
        JSONObject header = new JSONObject();
        header.put("vehicleVin",vehicleVin.getText().toString());
        header.put("plateNumber",plateNumber.getText().toString());
        header.put("make",make.getText().toString());
        header.put("model",model.getText().toString());
        header.put("year",Integer.parseInt(year.getText().toString()));
        header.put("vehicleType",vehicleType.getText().toString());
        header.put("vehicleColor",vehicleColor.getText().toString());
        header.put("userId",userDetail.getUserId());
        header.put("primaryVehicle", value);

        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.POST, endpoint, header, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Vehicle vehicle = new Vehicle((long) 111, vehicleVin.getText().toString(), plateNumber.getText().toString(),
                        make.getText().toString(), model.getText().toString(), year.getText().toString(), vehicleType.getText().toString(),
                        vehicleColor.getText().toString(), value, userDetail.getUserId());
                list.add(vehicle);
                adapter.notifyDataSetChanged();

                vehicleVin.setText("");
                plateNumber.setText("");
                make.setText("");
                model.setText("");
                year.setText("");
                vehicleType.setText("");
                vehicleColor.setText("");
                primaryVehicle.setChecked(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }) ;
        queue.add(jsonobject);
    }


    protected void modifyInputDialog(final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.vehicle_details, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setTitle("Update Vehicle Info");
        alertDialogBuilder.setView(promptView);
        vehicleVin = promptView.findViewById(R.id.vehicleVin);
        plateNumber = promptView.findViewById(R.id.plateNumber);
        make = promptView.findViewById(R.id.make);
        model = promptView.findViewById(R.id.model);
        year = promptView.findViewById(R.id.year);
        vehicleType = promptView.findViewById(R.id.vehicleType);
        vehicleColor = promptView.findViewById(R.id.vehicleColor);
        primaryVehicle = promptView.findViewById(R.id.primaryVehicle);
        if (primaryVehicle.isChecked())
            value = true;

        final Vehicle vehicle = list.get(position);
        vehicleVin.setText(vehicle.getVehicleVin());
        plateNumber.setText(vehicle.getPlateNumber());
        make.setText(vehicle.getMake());
        model.setText(vehicle.getModel());
        year.setText(vehicle.getYear());
        vehicleType.setText(vehicle.getVechicleType());
        vehicleColor.setText(vehicle.getVechicleColor());
        primaryVehicle.setChecked(vehicle.getPrimaryVehicle());

        alertDialogBuilder.setPositiveButton("Modify",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            modifyVehicle(position, vehicle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNeutralButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            apiMainDelete(position, vehicle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        actionListeners(promptView);
    }

    private void apiMainDelete(final int position, final Vehicle vehicle) throws JSONException {
        final String endpoint = getString(R.string.url) + "api/electricalVehicles/"+vehicle.getId();
        JSONObject header = new JSONObject();
        header.put("userId",userDetail.getUserId());
        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.DELETE, endpoint, header, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) ;
        queue.add(jsonobject);
    }

    private void modifyVehicle(int position, Vehicle vehicle) throws JSONException {
        apiDelete(position, vehicle);
    }

    private void apiDelete(final int position, final Vehicle vehicle) throws JSONException {
        final String endpoint = getString(R.string.url) + "api/electricalVehicles/"+vehicle.getId();
        JSONObject header = new JSONObject();
        header.put("userId",userDetail.getUserId());

        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.DELETE, endpoint, header, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    apiAdd(position, vehicle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) ;
        queue.add(jsonobject);
    }

    private void apiAdd(final int position, final Vehicle vehicle) throws JSONException {
        final String endpoint = getString(R.string.url) + "api/electricalVehicles";
        JSONObject header = new JSONObject();
        header.put("vehicleVin",vehicleVin.getText().toString());
        header.put("plateNumber",plateNumber.getText().toString());
        header.put("make",make.getText().toString());
        header.put("model",model.getText().toString());
        header.put("year",Integer.parseInt(year.getText().toString()));
        header.put("vehicleType",vehicleType.getText().toString());
        header.put("vehicleColor",vehicleColor.getText().toString());
        header.put("userId",userDetail.getUserId());
        header.put("primaryVehicle", value);

        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.POST, endpoint, header, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                adapter.UpdateData(position, vehicle);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) ;
        queue.add(jsonobject);
    }

    void actionListeners(View promptView){
        textInputvehicleVin =  promptView.findViewById(R.id.textInputvehicleVin);
        vehicleVin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && vehicleVin.getText().toString().isEmpty()) {
                    textInputvehicleVin.setError("Vehicle Number cannot be empty!");
                }
            }
        });

        vehicleVin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String vhvin = vehicleVin.getText().toString();
                validvehicleVin = false;
                if (vhvin.isEmpty()) {
                    textInputvehicleVin.setError("Vehicle Number cannot be empty!");
                }
                else {
                    validvehicleVin = true;
                    textInputvehicleVin.setError(null);
                    // enableSave();
                }
            }
        });

        textInputplateNumber= promptView.findViewById(R.id.textInputplateNumber);
        plateNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && plateNumber.getText().toString().isEmpty()) {
                    textInputplateNumber.setError("Plate Number cannot be empty!");
                }
            }
        });

        plateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pltnum = plateNumber.getText().toString();
                validplateNumber = false;
                if (pltnum.isEmpty()) {
                    textInputplateNumber.setError("Plate Number cannot be empty!");
                }
                else {
                    validplateNumber = true;
                    textInputplateNumber.setError(null);
                    // enableSave();
                }
            }
        });

        textInputmake  = promptView.findViewById(R.id.textInputmake);
        make.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && make.getText().toString().isEmpty()) {
                    textInputmake.setError("Make cannot be empty!");
                }
            }
        });

        make.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mk = make.getText().toString();
                validInputmake = false;
                if (mk.isEmpty()) {
                    textInputmake.setError("Make cannot be empty!");
                }
                else {
                    validInputmake = true;
                    textInputmake.setError(null);
                    // enableSave();
                }
            }
        });

        textInputmodel = promptView.findViewById(R.id.textInputmodel);
        model.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && model.getText().toString().isEmpty()) {
                    textInputmodel.setError("Vehicle Number cannot be empty!");
                }
            }
        });

        model.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phn = model.getText().toString();
                validmodel = false;
                if (phn.isEmpty()) {
                    textInputmodel.setError("Vehicle Number cannot be empty!");
                }
                else {
                    validmodel = true;
                    textInputmodel.setError(null);
                    // enableSave();
                }
            }
        });

        textInputyear = promptView.findViewById(R.id.textInputyear);
        year.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && year.getText().toString().isEmpty()) {
                    textInputyear.setError("Year cannot be empty!");
                }
            }
        });

        year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phn = year.getText().toString();
                validyear = false;
                if (phn.isEmpty()) {
                    textInputyear.setError("Year cannot be empty!");
                }
                else {
                    validyear = true;
                    textInputyear.setError(null);
                    // enableSave();
                }
            }
        });

        textInputvehicleType = promptView.findViewById(R.id.textInputvehicleType);
        vehicleType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && vehicleType.getText().toString().isEmpty()) {
                    textInputvehicleType.setError("Vehicle Number cannot be empty!");
                }
            }
        });

        vehicleType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phn = vehicleType.getText().toString();
                validvehicleType = false;
                if (phn.isEmpty()) {
                    textInputvehicleType.setError("Vehicle Number cannot be empty!");
                }
                else {
                    validvehicleType = true;
                    textInputvehicleType.setError(null);
                    // enableSave();
                }
            }
        });
    }

}
