package com.example.seccharge.Signup;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.seccharge.Authinfo;
import com.example.seccharge.Dashboard.Dashboard;
import com.example.seccharge.Intro.IntroActivity;
import com.example.seccharge.R;
import com.example.seccharge.Savesharedpreference;
import com.example.seccharge.navigation_drawer;
import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Signup extends AppCompatActivity {

    // INPUTS ARE CAPTURED AS VARIABLES
    private View register, security_questions, contact_info,address_info;
    private TextInputLayout textInputEmail, textInputUsername, textInputPassword,
            textInputConfirmPassword, Inputspinner1, Inputspinner2, textInputAnswer1,
            textInputAnswer2, textInputFirstname, textInputLastname, textInputphone, textInputfax, textInputCompany;
    private EditText userNameInput, emailInput, pwdInput, conPwdInput, answer1, answer2, firstName, lastName, phone, fax, company;
    Button next1, next2, next3, agree;
    private Spinner spinner1, spinner2;
    private TextView current_address;

    /* REGEX PATTERNS FOR VALIDATIONS
    1. UserName - Alpha Numeric chars,_&-
    2. Email - RFC822 Compliant Regex
    */
    private final String userPattern = "^[a-zA-Z0-9_-]+$";
    private final String namePattern = "^[a-zA-Z]+$";
    private final String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";



    // MOCKOON ENDPOINT AND QUEUE FOR API REQUESTS
    private RequestQueue queue;

    // BUTTON VARIABLE CHECKS
    boolean validusrname, validemail, validpwd, validconpwd, validques1, validans1, validques2, validans2,
            validfstname, validlstname, validphone, validfax, validcompany;

    // SPINNERS ADAPTER
    private ArrayAdapter<String> adapter1,adapter2,adapter3;
    private ArrayList<String> secQues, spinner2values, spinner3values;

    // SEQURITY QUESTION CHECK
    boolean ques1set = false;
    boolean ques2set = false;
    boolean secquesrisk1 = false;
    boolean secquesrisk2 = false;
    boolean secansrisk1 = false;
    boolean secansrisk2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        current_address=findViewById(R.id.selected_address);
        current_address.setVisibility(View.INVISIBLE);

        queue = Volley.newRequestQueue(this);
        userNameInput = findViewById(R.id.username);
        textInputUsername = findViewById(R.id.textInputUsername);
        userNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateUserName();
                }
            }
        });
        userNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrName = userNameInput.getText().toString();
                validusrname = false;
                if (usrName.isEmpty()) {
                    textInputUsername.setError("Username cannot be empty!");
                }
                else {
                    if (!usrName.matches(userPattern)) {
                        textInputUsername.setError("Can only contain alpha numerics,- and _");
                    } else {
                        validusrname = true;
                        textInputUsername.setError(null);
                    }
                    enableNext1();
                }
            }
        });


        emailInput = findViewById(R.id.email);
        textInputEmail = findViewById(R.id.textInputEmail);
        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = emailInput.getText().toString();
                validemail = false;
                if (email.isEmpty()) {
                    textInputEmail.setError("Email cannot be empty!");
                }
                else{
                    if (!email.matches(emailPattern)) {
                        textInputEmail.setError("Invalid email");

                    }
                    else{
                        validemail = true;
                        textInputEmail.setError(null);
                    }
                    enableNext1();
                }
            }
        });

        pwdInput = findViewById(R.id.password);
        textInputPassword = findViewById(R.id.textInputPassword);
        pwdInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && pwdInput.getText().toString().isEmpty()) {
                    textInputPassword.setError("Password cannot be empty!");
                }
            }
        });
        pwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePassword();
            }
        });

        conPwdInput = findViewById(R.id.ConfirmPassword);
        textInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);
        conPwdInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && conPwdInput.getText().toString().isEmpty()) {
                    textInputConfirmPassword.setError("Password cannot be empty!");
                }
            }
        });

        conPwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateConfirmPassword();
            }
        });

        register =findViewById(R.id.register);
        security_questions =findViewById(R.id.security_questions);
        contact_info =findViewById(R.id.contact_info);
        address_info =findViewById(R.id.address_info);
        address_info.setVisibility(View.INVISIBLE);
        security_questions.setVisibility(View.INVISIBLE);
        contact_info.setVisibility(View.INVISIBLE);


        String apiKey = "keep your key here";
        Places.initialize(getApplicationContext(), apiKey);


        next1 = findViewById(R.id.next1);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSecurityQuestions();
                register.setVisibility(View.GONE);
                security_questions.setVisibility(View.VISIBLE);
            }
        });


        spinner1 = findViewById(R.id.spinner1);
        Inputspinner1 = findViewById(R.id.Inputspinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int index = spinner1.getSelectedItemPosition();
                int index2 = spinner2.getSelectedItemPosition();
                validques1 = false;
                secquesrisk1 = false;
                if(ques1set){
                    if(index==spinner1.getAdapter().getCount()+1) {
                        Inputspinner1.setError("Question cannot be empty!");
                    }else if(index==index2) {
                        Inputspinner1.setError("SECURITY RISK: Both questions cannot be same");
                        secquesrisk1 = true;
                    }else if(secquesrisk2){
                        Inputspinner2.setError(null);
                    }
                    else{
                        validques1 = true;
                        Inputspinner1.setError(null);
                    }
                    enableNext2();
                }
                ques1set = true;
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinner1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int index = spinner1.getSelectedItemPosition();
                if (!hasFocus && (index==spinner1.getAdapter().getCount()+1)) {
                    Inputspinner1.setError("Question cannot be empty!");
                }
            }
        });

        answer1 = findViewById(R.id.answer1);
        textInputAnswer1 = findViewById(R.id.textInputAnswer1);
        answer1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && answer1.getText().toString().isEmpty()) {
                    textInputAnswer1.setError("The answer cannot be empty!");
                }
            }
        });

        answer1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ans1 = answer1.getText().toString();
                String ans2 = answer2.getText().toString();
                // Log.d("ans1", ans1);
                // Log.d("ans2", ans2);
                validans1 = false;
                secansrisk1 = false;
                if(ans1.isEmpty()){
                    textInputAnswer1.setError("Answer cannot be empty!");
                }else if(ans1.equalsIgnoreCase(ans2)) {
                    textInputAnswer1.setError("SECURITY RISK: Both answers cannot be same");
                    secansrisk1 = true;
                }else if(secansrisk2){
                    textInputAnswer2.setError(null);
                }
                else{
                    validans1 = true;
                    textInputAnswer1.setError(null);
                }
                enableNext2();
            }
        });

        spinner2 = findViewById(R.id.spinner2);
        Inputspinner2 = findViewById(R.id.Inputspinner2);
        spinner2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int index = spinner2.getSelectedItemPosition();
                if (!hasFocus && (index==spinner2.getAdapter().getCount()+1)) {
                    Inputspinner2.setError("Question cannot be empty!");
                }
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int index = spinner2.getSelectedItemPosition();
                int index1 = spinner1.getSelectedItemPosition();
                // Log.d("index1", "onItemSelected: "+String.valueOf(index1));
                // Log.d("index2", "onItemSelected: "+String.valueOf(index));

                validques2 = false;
                secquesrisk2 = false;

                if (ques2set) {
                    if (index==spinner2.getAdapter().getCount()+1) {
                        Inputspinner2.setError("Question cannot be empty!");
                    }
                    else if (index == index1) {
                        Inputspinner2.setError("SECURITY RISK: Both questions cannot be same");
                        secquesrisk2 = true;
                    }else if(secquesrisk1){
                        Inputspinner1.setError(null);
                    }
                    else {
                        validques2 = true;
                        Inputspinner2.setError(null);
                    }
                    enableNext2();
                }
                ques2set=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        answer2 = findViewById(R.id.answer2);
        textInputAnswer2 = findViewById(R.id.textInputAnswer2);
        answer2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && answer2.getText().toString().isEmpty()) {
                    textInputAnswer2.setError("The answer cannot be empty!");
                }
            }
        });

        answer2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ans1 = answer1.getText().toString();
                String ans2 = answer2.getText().toString();
                validans2 = false;
                secansrisk2 = false;
                if(ans2.isEmpty()){
                    textInputAnswer2.setError("Answer cannot be empty!");
                }else if(ans1.equalsIgnoreCase(ans2)) {
                    textInputAnswer2.setError("SECURITY RISK: Both answers cannot be same");
                    secansrisk2 = true;
                }else if(secansrisk1){
                    textInputAnswer1.setError(null);
                }
                else{
                    validans2 = true;
                    textInputAnswer2.setError(null);
                }
                enableNext2();
            }
        });

        next2 = findViewById(R.id.next2);
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                security_questions.setVisibility(View.GONE);
                contact_info.setVisibility(View.VISIBLE);
            }
        });

        firstName = findViewById(R.id.firstName);
        textInputFirstname = findViewById(R.id.textInputFirstname);
        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && firstName.getText().toString().isEmpty()) {
                    textInputFirstname.setError("FirstName cannot be empty!");
                }
            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fstName = firstName.getText().toString();
                validfstname = false;
                if (fstName.isEmpty()) {
                    textInputFirstname.setError("FirstName cannot be empty!");
                }
                else {
                    if (!fstName.matches(namePattern)) {
                        textInputFirstname.setError("Only letters are allowed");
                    } else {
                        validfstname = true;
                        textInputFirstname.setError(null);
                    }
                    enableNext3();
                }
            }
        });

        lastName = findViewById(R.id.lastName);
        textInputLastname = findViewById(R.id.textInputLastname);
        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && lastName.getText().toString().isEmpty()) {
                    textInputLastname.setError("LastName cannot be empty!");
                }
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String lstName = lastName.getText().toString();
                validlstname = false;
                if (lstName.isEmpty()) {
                    textInputLastname.setError("LastName cannot be empty!");
                }
                else {
                    if (!lstName.matches(namePattern)) {
                        textInputLastname.setError("Only letters are allowed");
                    } else {
                        validlstname = true;
                        textInputLastname.setError(null);
                    }
                    enableNext3();
                }
            }
        });


        phone = findViewById(R.id.phone);
        textInputphone = findViewById(R.id.textInputphone);
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && phone.getText().toString().isEmpty()) {
                    textInputphone.setError("Phone Number cannot be empty!");
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phn = phone.getText().toString();
                validphone = false;
                if (phn.isEmpty()) {
                    textInputphone.setError("phone Number cannot be empty!");
                }
                else {
                    if (phn.length()>10) {
                        textInputphone.setError("Cannot be more than 10");
                    } else {
                        validphone = true;
                        textInputphone.setError(null);
                    }
                    enableNext3();
                }
            }
        });


        company = findViewById(R.id.companyName);
        textInputCompany = findViewById(R.id.textInputcompany);
        company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String comp = company.getText().toString();
                validcompany = false;
                if (!comp.matches(userPattern)) {
                    textInputCompany.setError("Can only contain alpha numerics,- and _");
                } else {
                    validcompany = true;
                    textInputCompany.setError(null);
                }
                enableNext3();
            }
        });


        next3 = findViewById(R.id.next3);
        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact_info.setVisibility(View.GONE);
                address_info.setVisibility(View.VISIBLE);

            }
        });


        agree = findViewById(R.id.cirAgree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Places.initialize(getApplicationContext(), "keep your key here");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                current_address.setVisibility(View.VISIBLE);
                current_address.setText(place.getAddress());
                agree.setBackgroundResource(R.drawable.login_button_bk);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void saveUser() throws JSONException {
        JSONObject secQues = new JSONObject();
        JSONObject queIds = new JSONObject();
        queIds.put("id", spinner1.getSelectedItemPosition());
        secQues.put("securityQuestion", queIds);
        secQues.put("answer", answer1.getText().toString());

        ArrayList<JSONObject> secChargeQues = new ArrayList<>();
        secChargeQues.add(secQues);

        JSONObject metadata = new JSONObject();
        metadata.put("email", emailInput.getText().toString());
        metadata.put("firstName", firstName.getText().toString());
        metadata.put("lastName", lastName.getText().toString());

        /*
        THESE ARE NOT ADDED AS SINCE THESE ATTRIBUTES ARE NOT EXPOSED
        IN THE API HEADER REQUEST PROVIDED

        metadata.put("companyName", company.getText().toString());
        metadata.put("dateCreated");
        metadata.put("companyName");
        metadata.put("homePhone");
        metadata.put("address2");
        metadata.put("city");
        metadata.put("province");
        metadata.put("lastName");
        metadata.put("companyName");
        metadata.put("firstName");
        metadata.put("lastName");
        metadata.put("companyName");
        */

        JSONObject header = new JSONObject();
        header.put("password", pwdInput.getText().toString());
        header.put("secChargeQuestions", secChargeQues);
        header.put("username",userNameInput.getText().toString());
        header.put("hasTwoFactorAuth", false);
        header.put("userMetadata", metadata);

        String cmd = getString(R.string.url)+ "api/users";
        JsonObjectRequest request = new JsonObjectRequest(cmd, header,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            Authinfo detail = new Authinfo(response.getString("token"), user.getLong("id"), user.getBoolean("active"));
                            Savesharedpreference.setUserDetails(getApplicationContext(), detail);

                            Intent introAct = new Intent(Signup.this, IntroActivity.class);
                            startActivity(introAct);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("validateUserName", "onErrorResponse: "+error.toString());
            }
        });
        queue.add(request);
    }

    private void validateUserName() {
        validusrname = false;
        String usrName = userNameInput.getText().toString();
        if(!usrName.isEmpty()) {
            if (usrName.matches(userPattern)) {
                textInputUsername.setError(null);
                HashMap<String, String> params = new HashMap<>();
                params.put("usernameOrEmail", usrName);
                String cmd = getString(R.string.url)+ "api/users/isUnique";
                Log.d("endpoint", "validateUserName: "+cmd);
                JsonObjectRequest request = new JsonObjectRequest(cmd, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    boolean unique = response.getBoolean("isUnique");
                                    if (unique) {
                                        textInputUsername.setError("Username is already taken!");
                                    } else {
                                        // need to show hint here
                                        textInputUsername.setError(null);
                                        validusrname = true;
                                    }
                                    enableNext1();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("validateUserName", "onErrorResponse: "+error.toString());
                    }
                });
                queue.add(request);
            }
        }
        else {
            textInputUsername.setError("Username cannot be empty!");
        }
    }

    private void validateEmail() {
        String email = emailInput.getText().toString();
        validemail = false;
        if(!email.isEmpty()){
            if (email.matches(emailPattern)) {
                textInputEmail.setError(null);
                HashMap<String, String> params = new HashMap<>();
                params.put("usernameOrEmail", email);
                String cmd = getString(R.string.url) + "api/users/isUnique";
                JsonObjectRequest request = new JsonObjectRequest(cmd, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    boolean unique = response.getBoolean("isUnique");
                                    if (unique) {
                                        textInputEmail.setError("Email is already taken!");
                                    } else {
                                        // Need to show hint here
                                        textInputEmail.setError(null);
                                        validemail = true;
                                    }
                                    enableNext1();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("validateEmail", "onErrorResponse: "+error.getMessage());
                    }
                });
                queue.add(request);
            }
        }
        else {
            textInputEmail.setError("Email cannot be empty!");
        }

    }

    private void validatePassword() {
        validpwd = false;
        String pwd = pwdInput.getText().toString();
        String conpwd = conPwdInput.getText().toString();
        if(!pwd.isEmpty()){
            if((pwd.length() < 10) && (pwd.contains(" "))) {
                textInputPassword.setError("Should be atleast 10 characters long\nBlank space not allowed");
            }
            else if(pwd.length() < 10){
                textInputPassword.setError("Should be atleast 10 characters long");
            }
            else if(!conpwd.equals(pwd) && !conpwd.isEmpty()){
                textInputPassword.setError("Passwords do not match!");
            }
            else{
                textInputPassword.setError(null);
                validpwd = true;
            }
        }
        else {
            textInputPassword.setError("Password cannot be empty!");
        }
        enableNext1();

    }

    private void validateConfirmPassword() {
        validconpwd = false;
        String pwd = pwdInput.getText().toString();
        String conpwd = conPwdInput.getText().toString();
        if(!conpwd.isEmpty()){
            if((conpwd.length() < 10) && (conpwd.contains(" "))) {
                textInputConfirmPassword.setError("Should be atleast 10 characters long\nBlank space not allowed");
            }
            else if(conpwd.length() < 10){
                textInputConfirmPassword.setError("Should be atleast 10 characters long");
            }
            else if(!conpwd.equals(pwd)){
                textInputConfirmPassword.setError("Passwords do not match!");
            }
            else{
                textInputConfirmPassword.setError(null);
                validconpwd = true;
            }
        }
        else { textInputConfirmPassword.setError("Password cannot be empty!");
        }
        enableNext1();
    }


    private void enableNext1(){
        if(validusrname && validemail && validpwd && validconpwd){
            next1.setEnabled(true);
            next1.setBackgroundResource(R.drawable.login_button_bk);
        } else{
            next1.setEnabled(false);
            next1.setBackgroundResource(R.drawable.disable_mode_button);
        }
    }

    private void enableNext2(){
        if(validques1 && validans1 && validques2 && validans2){
            next2.setEnabled(true);
            next2.setBackgroundResource(R.drawable.login_button_bk);
        } else{
            next2.setEnabled(false);
            next2.setBackgroundResource(R.drawable.disable_mode_button);
        }
    }

    private void enableNext3(){
        if(validfstname && validlstname && validphone){
            if(company.getText().toString().isEmpty()) {
                next3.setEnabled(true);
                next3.setBackgroundResource(R.drawable.login_button_bk);
            }
            else if(validcompany){
                next3.setEnabled(true);
                next3.setBackgroundResource(R.drawable.login_button_bk);
            }

        }
        else{
            next3.setEnabled(false);
            next3.setBackgroundResource(R.drawable.disable_mode_button);
        }
    }

    private void getSecurityQuestions() {
        String cmd = getString(R.string.url) + "api/securityQuestions";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, cmd, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    secQues = new ArrayList<>();
                    for(int i=0;i<response.length();i++) {
                        JSONObject ques = response.getJSONObject(i);
                        secQues.add(ques.getString("securityQuestion"));
                    }
                    secQues.add("Select a question*");
                    final int listsize = secQues.size() - 1;
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Signup.this,android.R.layout.simple_spinner_item, secQues) {
                        @Override
                        public int getCount() {
                            return(listsize);
                        }
                    };
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);
                    spinner1.setSelection(listsize);
                    spinner2.setAdapter(adapter1);
                    spinner2.setSelection(listsize);

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Failing .......");
            }
        });
        queue.add(jsonArrayRequest);
    }

}






