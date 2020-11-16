package com.example.seccharge;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;
import com.example.seccharge.Login.LoginActivity;
import com.example.seccharge.Signup.Signup;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class profile extends Fragment {

    private User user;
    private TextView fullName, address, phone, companyName, joinedAt, username, password, email, securityQuestions;

    private TextInputLayout textInputEmail, textInputUsername, textInputPassword,
            Inputspinner1, Inputspinner2, textInputAnswer1,
            textInputAnswer2, textInputFirstname, textInputLastname, textInputphone, textInputfax, textInputCompany;
    private EditText userNameInput, emailInput, pwdInput, answer1, answer2, firstNameInput, lastNameInput, addressInput, phoneInput, fax, company;
    private Spinner spinner1, spinner2;

    private final String userPattern = "^[a-zA-Z0-9_-]+$";
    private final String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    // MOCKOON ENDPOINT AND QUEUE FOR API REQUESTS
    private RequestQueue queue;
    Authinfo authInfo;

    // BUTTON VARIABLE CHECKS
    boolean validusrname, validemail, validpwd, validconpwd, validques1, validans1, validques2, validans2,
            validfstname, validlstname, validphone, validfax, validcompany, ques1set, ques2set,
            secquesrisk1, secquesrisk2, secansrisk1, secansrisk2 = false;

    // SPINNERS ADAPTER
    private ArrayAdapter<String> adapter1,adapter2,adapter3;
    private ArrayList<String> secQues, spinner2values, spinner3values;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
        setHasOptionsMenu(true);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        fullName  = getView().findViewById(R.id.fullName);
        address  = getView().findViewById(R.id.address);
        phone  = getView().findViewById(R.id.phone);
        companyName  = getView().findViewById(R.id.companyName);
        joinedAt  = getView().findViewById(R.id.joinedAt);
        username  = getView().findViewById(R.id.username);
        password  = getView().findViewById(R.id.password);
        email  = getView().findViewById(R.id.email);
        securityQuestions  = getView().findViewById(R.id.securityQuestions);

        // Get authentication details
        authInfo = Savesharedpreference.getUserDetails(getContext());
        if(authInfo!= null)
        {
            profileDeatils();
        }
    }

    public void profileDeatils(){
        // String cmd = getString(R.string.url)+"api/users/"+authInfo.getUserId();
        String cmd = getString(R.string.url)+"api/users/3";
        Log.d("entering", cmd);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, cmd, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject userMetadata = response.getJSONObject("userMetadata");
                            String username = response.getString("username");
                            String password = response.getString("password");
                            boolean active = response.getBoolean("active");
                            boolean locked = response.getBoolean("locked");
                            boolean twoFactorAuthEnabled = response.getBoolean("twoFactorAuthEnabled");
                            String firstName = userMetadata.getString("firstName");
                            String lastName = userMetadata.getString("lastName");
                            String companyName = userMetadata.getString("companyName");
                            String email = userMetadata.getString("email");
                            String dateCreated = userMetadata.getString("dateCreated");
                            String expiryDate = userMetadata.getString("expiryDate");
                            String address1 = userMetadata.getString("address1");
                            String address2 = userMetadata.getString("address2");
                            String city = userMetadata.getString("city");
                            String province = userMetadata.getString("province");
                            String postalCode = userMetadata.getString("postalCode");
                            String country = userMetadata.getString("country");
                            String homePhone = userMetadata.getString("homePhone");
                            String cellPhone = userMetadata.getString("cellPhone");
                            String fax = userMetadata.getString("fax");
                            String formattedAddress = userMetadata.getString("formattedAddress");
                            Map<String, String> securityQuestions = new HashMap<String, String>();
                            JSONArray secQues = response.getJSONArray("userQuestions");
                            for(int i=0;i<secQues.length();i++) {
                                JSONObject secQue = secQues.getJSONObject(i);
                                JSONObject secques = secQue.getJSONObject("securityQuestion");
                                securityQuestions.put(secques.getString("securityQuestion"),
                                        secQue.getString("answer"));
                            }

                            user = new User(username, password, active, locked, twoFactorAuthEnabled,
                                    firstName, lastName, companyName, email, dateCreated, expiryDate, address1, address2,
                                    city, province, postalCode, country, homePhone, cellPhone, fax, formattedAddress, securityQuestions);

                            setProfile();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("error", "onResponse: "+e);
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

    @SuppressLint("SetTextI18n")
    public void setProfile(){
        fullName.setText("Name\n\n"+user.getFirstName()+" "+user.getLastName());
        address.setText("Address\n\n"+user.getFormattedAddress());
        phone.setText("Phone\n\n"+user.getCellPhone());
        companyName.setText("companyName\n\n"+user.getCompanyName());
        joinedAt.setText("Joined At\n\n"+user.getDateCreated());
        username.setText("User Name\n\n"+user.getUsername());
        password.setText("Password\n\n"+user.getPassword());
        email.setText("Email\n\n"+user.getEmail());
        StringBuilder ques = new StringBuilder();
        ques.append("\n");
        Log.d("questions", user.getSecurityQuestions().keySet().toString());
        if(user.getSecurityQuestions().size()>0) {
            List<String> temp = new ArrayList<>(user.getSecurityQuestions().keySet());
            for (int i = 0; i < temp.size(); i++) {
                ques.append(temp.get(i));
                ques.append("\n");
            }
            securityQuestions.setText("Security Questions"+ques);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit:
                modifyInputDialog();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void profileUpdate() throws JSONException {

        // user.setSecurityQuestions();

        JSONObject userData = new JSONObject();
        userData.put("id", authInfo.getUserId());
        userData.put("username", user.getUsername());
        userData.put("active", user.isActive());
        userData.put("locked", user.isLocked());
        userData.put("twoFactorAuthEnabled", user.isTwoFactorAuthEnabled());

        JSONObject metadata = new JSONObject();
        metadata.put("id", authInfo.getUserId());
        metadata.put("firstName", user.getFirstName());
        metadata.put("lastName", user.getLastName());
        metadata.put("companyName", user.getCompanyName());
        metadata.put("email", user.getEmail());
        metadata.put("dateCreated", user.getDateCreated());
        metadata.put("expiryDate", user.getExpiryDate());
        metadata.put("address1", user.getAddress1());
        metadata.put("address2", user.getAddress2());
        metadata.put("city", user.getCity());
        metadata.put("province", user.getProvince());
        metadata.put("postalCode", user.getPostalCode());
        metadata.put("country", user.getCountry());
        metadata.put("homePhone", user.getHomePhone());
        metadata.put("cellPhone", user.getCellPhone());
        metadata.put("fax", user.getFax());
        metadata.put("formattedAddress", user.getFormattedAddress());

        userData.put("userMetadata", metadata);
        userData.put("flag", false);

        String cmd = getString(R.string.url)+ "api/users";
        JsonObjectRequest request = new JsonObjectRequest(cmd, userData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setProfile();
                            Toast.makeText(getActivity().getApplicationContext(),"Profile has been updated",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Profile has been reverted back",Toast.LENGTH_SHORT).show();
                Log.d("validateUserName", "onErrorResponse: "+error.toString());
            }
        });
        queue.add(request);
    }

    private void modifyInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.activity_profile_detail, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Update Profile Info");
        alertDialogBuilder.setView(promptView);

        alertDialogBuilder.setPositiveButton("Modify",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        try {
                            user.setUsername(userNameInput.getText().toString());
                            user.setPassword(pwdInput.getText().toString());
                            user.setFirstName(firstNameInput.getText().toString());
                            user.setLastName(lastNameInput.getText().toString());
                            user.setCompanyName(company.getText().toString());
                            user.setEmail(emailInput.getText().toString());
                            user.setCellPhone(phoneInput.getText().toString());
                            user.setFormattedAddress(addressInput.getText().toString());

                            profileUpdate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        addressInput = promptView.findViewById(R.id.address);
        addressInput.setText(user.getFormattedAddress());

        userNameInput = promptView.findViewById(R.id.username);
        userNameInput.setText(user.getUsername());
        textInputUsername = promptView.findViewById(R.id.textInputUsername);
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

        pwdInput = promptView.findViewById(R.id.password);
        pwdInput.setText(user.getPassword());
        textInputPassword = promptView.findViewById(R.id.textInputPassword);
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

        emailInput = promptView.findViewById(R.id.email);
        emailInput.setText(user.getEmail());
        textInputEmail = promptView.findViewById(R.id.textInputEmail);
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

        spinner1 = promptView.findViewById(R.id.spinner1);
        Inputspinner1 = promptView.findViewById(R.id.Inputspinner1);
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

        answer1 = promptView.findViewById(R.id.answer1);
        answer1.setText(ans.get(0));
        textInputAnswer1 = promptView.findViewById(R.id.textInputAnswer1);
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

        spinner2 = promptView.findViewById(R.id.spinner2);
        Inputspinner2 = promptView.findViewById(R.id.Inputspinner2);
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

        answer2 = promptView.findViewById(R.id.answer2);
        answer2.setText(ans.get(1));
        textInputAnswer2 = promptView.findViewById(R.id.textInputAnswer2);
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


        firstNameInput = promptView.findViewById(R.id.firstName);
        firstNameInput.setText(user.getFirstName());
        /*
        textInputFirstname = promptView.findViewById(R.id.textInputFirstName);
        firstNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && firstNameInput.getText().toString().isEmpty()) {
                    textInputFirstname.setError("FirstName cannot be empty!");
                }
            }
        });
        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fstName = firstNameInput.getText().toString();
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
        */

        lastNameInput = promptView.findViewById(R.id.lastName);
        lastNameInput.setText(user.getLastName());
        /*
        textInputLastname = promptView.findViewById(R.id.textInputLastname);
        lastNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && lastNameInput.getText().toString().isEmpty()) {
                    textInputLastname.setError("LastName cannot be empty!");
                }
            }
        });
        lastNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String lstName = lastNameInput.getText().toString();
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
        phoneInput = promptView.findViewById(R.id.phone);
        phoneInput.setText(user.getCellPhone());
        textInputphone = promptView.findViewById(R.id.textInputphone);
        phoneInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && phoneInput.getText().toString().isEmpty()) {
                    textInputphone.setError("Phone Number cannot be empty!");
                }
            }
        });
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phn = phoneInput.getText().toString();
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

        company = promptView.findViewById(R.id.companyName);
        company.setText(user.getCompanyName());
        textInputCompany = promptView.findViewById(R.id.textInputcompany);
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

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    private void validateUserName() {
        validusrname = false;
        String usrName = userNameInput.getText().toString();
        if(!usrName.isEmpty()) {
            if (usrName.matches(userPattern)) {
                textInputUsername.setError(null);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("usernameOrEmail", usrName);
                String cmd = getString(R.string.url) + "users/isUnique";
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
                String cmd = getString(R.string.url) + "users/isUnique";
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
        String cmd = getString(R.string.url) + "securityQuestions";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, cmd, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    secQues = new ArrayList<>();
                    for(int i=0;i<response.length();i++) {
                        JSONObject ques = response.getJSONObject(i);
                        secQues.add(ques.getString("securityQuestion"));
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, secQues);
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

}

