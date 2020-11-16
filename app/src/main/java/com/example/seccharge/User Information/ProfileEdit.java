package com.example.seccharge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.seccharge.Signup.Signup;
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileEdit extends AppCompatActivity {

    private TextInputLayout textInputEmail, textInputUsername, textInputPassword,
            Inputspinner1, Inputspinner2, textInputAnswer1,
            textInputAnswer2, textInputFirstname, textInputLastname, textInputphone, textInputfax, textInputCompany;
    private EditText userNameInput, emailInput, pwdInput, answer1, answer2, firstName, lastName, phone, fax, company;
    private Spinner spinner1, spinner2;
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
    private String URL = "http://10.0.2.2:3000/api/";

    // BUTTON VARIABLE CHECKS
    boolean validusrname, validemail, validpwd, validconpwd, validques1, validans1, validques2, validans2,
            validfstname, validlstname, validphone, validfax, validcompany, ques1set, ques2set,
            secquesrisk1, secquesrisk2, secansrisk1, secansrisk2 = false;

    // SPINNERS ADAPTER
    private ArrayAdapter<String> adapter1,adapter2,adapter3;
    private ArrayList<String> secQues, spinner2values, spinner3values;

    private com.example.seccharge.User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences mPrefs = getSharedPreferences("Userdata", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        user = gson.fromJson(json, com.example.seccharge.User.class);

        queue = Volley.newRequestQueue(this);
        userNameInput = findViewById(R.id.username);
        userNameInput.setText(user.getUsername());
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
                }
            }
        });

        pwdInput = findViewById(R.id.password);
        pwdInput.setText(user.getPassword());
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

        emailInput = findViewById(R.id.email);
        emailInput.setText(user.getEmail());
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
                }
            }
        });

        getSecurityQuestions();
        List<String> ques= new ArrayList<>(user.getSecurityQuestions().keySet());
        List<String> ans= new ArrayList<>(user.getSecurityQuestions().values());

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
                    enableSave();
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
        answer1.setText(ans.get(0));
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
                Log.d("ans1", ans1);
                Log.d("ans2", ans2);
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
                enableSave();
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
                // your code here
                int index = spinner2.getSelectedItemPosition();
                int index1 = spinner1.getSelectedItemPosition();
                Log.d("index1", "onItemSelected: "+String.valueOf(index1));
                Log.d("index2", "onItemSelected: "+String.valueOf(index));

                validques2 = false;
                secquesrisk2 = false;

                if (ques2set) {
                    if (index==spinner2.getAdapter().getCount()+1) {
                        Inputspinner2.setError("Question cannot be empty!");
                    }
                    else if (index == index1) {
                        Log.d("hey", "onItemSelected: ");
                        Inputspinner2.setError("SECURITY RISK: Both questions cannot be same");
                        secquesrisk2 = true;
                    }else if(secquesrisk1){
                        Inputspinner1.setError(null);
                    }
                    else {
                        validques2 = true;
                        Inputspinner2.setError(null);
                    }
                    enableSave();
                }
                ques2set=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        answer2 = findViewById(R.id.answer2);
        answer2.setText(ans.get(1));
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
                enableSave();
            }
        });


        /*
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
                    enableSave();
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
                    enableSave();
                }
            }
        });

        */

        phone = findViewById(R.id.phone);
        phone.setText(user.getCellPhone());
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
                    enableSave();
                }
            }
        });


        company = findViewById(R.id.companyName);
        company.setText(user.getCompanyName());
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
                enableSave();
            }
        });

    }

    private void validateUserName() {
        validusrname = false;
        String usrName = userNameInput.getText().toString();
        if(!usrName.isEmpty()) {
            if (usrName.matches(userPattern)) {
                textInputUsername.setError(null);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("usernameOrEmail", usrName);
                String cmd = URL + "users/isUnique";
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
                                    enableSave();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("usernameOrEmail", email);
                String cmd = URL + "users/isUnique";
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
                                    enableSave();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
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
        if(!pwd.isEmpty()){
            if((pwd.length() < 10) && (pwd.contains(" "))) {
                textInputPassword.setError("Should be atleast 10 characters long\nBlank space not allowed");
            }
            else if(pwd.length() < 10){
                textInputPassword.setError("Should be atleast 10 characters long");
            }

            else{
                textInputPassword.setError(null);
                validpwd = true;
            }
        }
        else {
            textInputPassword.setError("Password cannot be empty!");
        }
        enableSave();
    }

    private void enableSave(){

    }

    private void getSecurityQuestions() {
        String cmd = URL + "securityQuestions";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, cmd, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    secQues = new ArrayList<>();
                    for(int i=0;i<response.length();i++) {
                        JSONObject ques = response.getJSONObject(i);
                        secQues.add(ques.getString("securityQuestion"));
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ProfileEdit.this,android.R.layout.simple_spinner_item, secQues);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);
                    List<String> temp= new ArrayList<>(user.getSecurityQuestions().keySet());
                    spinner1.setSelection(adapter1.getPosition(temp.get(0)));
                    spinner2.setAdapter(adapter1);
                    spinner2.setSelection(adapter1.getPosition(temp.get(1)));
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
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}
