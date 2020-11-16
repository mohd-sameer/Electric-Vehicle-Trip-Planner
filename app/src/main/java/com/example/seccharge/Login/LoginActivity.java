package com.example.seccharge.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;
import com.example.seccharge.Intro.IntroActivity;
import com.example.seccharge.R;
import com.example.seccharge.Signup.Forgot_Password;
import com.example.seccharge.Signup.Signup;
import com.example.seccharge.Authinfo;
import com.example.seccharge.Savesharedpreference;
import com.example.seccharge.navigation_drawer;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    private EditText emailInput,passwordInput;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    TextView signup,forgot_password;
    boolean validemail, validpwd;
    private int statusCode = 0;
    private RequestQueue queue;
    private Authinfo authInfo;
    private Button login;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        validemail =false;
        validpwd = false;
        authInfo = Savesharedpreference.getUserDetails(LoginActivity.this);
        if(authInfo!= null)
        {
            String token = authInfo.getToken();
            JWT jwt = new JWT(token);
            Date expiresAt = jwt.getExpiresAt();
            Date currentDate = new Date();
            // Log.d("expire", "onCreate: "+expiresAt);
            // Log.d("expire", "onCreate: "+currentDate);
            assert expiresAt != null;
            if(expiresAt.before(currentDate)){
                try {
                    refreshToken();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // STAY WITH LOGIN ACTIVITY
        setContentView(R.layout.activity_login);
        textInputEmail = findViewById(R.id.textInputEmail);
        emailInput = findViewById(R.id.editTextEmail);
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
                    textInputEmail.setError("Email/Username cannot be empty!");
                } else {
                    validemail = true;
                    textInputEmail.setError(null);
                }
                enableLogin();
            }

        });

        textInputPassword = findViewById(R.id.textInputPassword);
        passwordInput = findViewById(R.id.editTextPassword);
        passwordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && passwordInput.getText().toString().isEmpty()) {
                    textInputPassword.setError("Password cannot be empty!");
                }
            }
        });
        passwordInput.addTextChangedListener(new TextWatcher() {
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

        login = findViewById(R.id.cirAgree);
        login.setBackgroundResource(R.drawable.disable_mode_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i =new Intent(LoginActivity.this, navigation_drawer.class);
              //  startActivity(i);
                try {
                    authentication();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Forgot_Password.class);
                startActivity(i);
            }
        });
        signup = findViewById(R.id.signupno);
        signup.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Intent intent = new Intent(LoginActivity.this, Signup.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }


    private void refreshToken() throws JSONException {
        final String endPoint =   getString(R.string.url)+"api/users/refreshToken";
        // Log.d("Endpoint",endPoint);
        JSONObject data = new JSONObject();
        data.put("token", authInfo.getToken());
        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.POST, endPoint, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    if(statusCode==200) {
                        authInfo.setToken(response.getString("token"));
                        Savesharedpreference.clearUserDetails(getApplicationContext());
                        Savesharedpreference.setUserDetails(getApplicationContext(), authInfo);
                        introActivity(authInfo);
                    }
                    else {
                        Log.d("refreshToken:", response.getString("error"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("refreshToken", String.valueOf(error));
                Toast.makeText(LoginActivity.this, "Something went wrong"+error+"." +
                        "Please try logging in with username and password" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
                if(response!=null){
                    statusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(jsonobject);
    }

    private void introActivity(Authinfo det){
        // THIS WAY IF THE REFRESH TOKEN FAILS INTRO ACTIVITY STILL COMES IN PLACE
        Intent intent;
        if(!det.isIntroOpened()) {
            intent= new Intent(LoginActivity.this, IntroActivity.class);
            det.setIntroOpened(true);
            Savesharedpreference.clearUserDetails(getApplicationContext());
            Savesharedpreference.setUserDetails(getApplicationContext(), det);
        }else{
            intent= new Intent(getApplicationContext(), navigation_drawer.class);
        }
        startActivity(intent);
    }

    private void authentication() throws JSONException {
        final String endPoint =   getString(R.string.url)+"api/login";
        JSONObject data = new JSONObject();
        data.put("usernameOrEmail", emailInput.getText().toString());
        data.put("password", passwordInput.getText().toString());
        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.POST, endPoint, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    if(statusCode==200) {
                        JSONObject user = response.getJSONObject("user");
                        Authinfo detail = new Authinfo(response.getString("token"), user.getLong("id"), user.getBoolean("active"));
                        Savesharedpreference.setUserDetails(getApplicationContext(), detail);
                        introActivity(detail);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login Failed : Incorrect credentials" , Toast.LENGTH_SHORT).show();
                        Log.d("Error", "Failing .......");
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
                if(response!=null){
                    statusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsonobject);
    }

    private void validateEmail() {
        validemail = false;
        if (emailInput.getText().toString().isEmpty()) {
            textInputEmail.setError("Email cannot be empty");
        }
        else{
            validemail = true;
            enableLogin();
        }
    }

    private void validatePassword(){
        validpwd = false;
        String pwd = passwordInput.getText().toString();
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
        enableLogin();
    }

    private void enableLogin(){
        if(validemail && validpwd){
            login.setEnabled(true);
            login.setBackgroundResource(R.drawable.login_button_bk);
        } else{
            login.setEnabled(false);
            login.setBackgroundResource(R.drawable.disable_mode_button);
        }
    }

}




