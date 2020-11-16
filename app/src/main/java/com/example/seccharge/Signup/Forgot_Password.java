package com.example.seccharge.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.seccharge.Login.LoginActivity;
import com.example.seccharge.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Forgot_Password extends AppCompatActivity {

    private EditText emailUsernameInput, security_answer;
    private TextView security_question;
    private TextInputLayout textInputEmailUsername, textInputAnswer;

    private View usrques,usrname,back_to_login_layout;
    Button next_security,reset_pass,back_to_login_button;
    boolean validemail;
    RequestQueue queue;
    boolean validans;
    ArrayList<JSONObject> secQues;
    JSONObject secqBuild;
    private int statusCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        validemail =false;
        validans = false;
        queue = Volley.newRequestQueue(this);
        secqBuild = new JSONObject();

        usrname=findViewById(R.id.reset_username);
        usrques=findViewById(R.id.reset_security);
        back_to_login_layout=findViewById(R.id.backtologin);
       back_to_login_layout.setVisibility(View.INVISIBLE);
       usrques.setVisibility(View.INVISIBLE);

       next_security=findViewById(R.id.forgot_security_next);
       reset_pass=findViewById(R.id.reset_pass);
       back_to_login_button=findViewById(R.id.backtologin_button);

       next_security.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   secqBuild.put("usernameOrEmail", emailUsernameInput);
                   getSecurityQuestions();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });

       reset_pass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   secqBuild.put("answer", security_answer.getText().toString());
                   validateSecurityAns();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
       back_to_login_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(Forgot_Password.this, LoginActivity.class);
               startActivity(i);
           }
       });


        textInputEmailUsername = findViewById(R.id.textInputEmail);
        emailUsernameInput = findViewById(R.id.editTextEmail);
        emailUsernameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });
        emailUsernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableNext1();
            }

        });

        security_question = findViewById(R.id.security_question);
        textInputAnswer = findViewById(R.id.textInputAnswer);
        security_answer = findViewById(R.id.security_answer);
        security_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && security_answer.getText().toString().isEmpty()) {
                    textInputAnswer.setError("The answer cannot be empty!");
                }
            }
        });

        security_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ans = security_answer.getText().toString();
                Log.d("ans", ans);
                validans = false;
                if(ans.isEmpty()){
                    textInputAnswer.setError("Answer cannot be empty!");
                }
                else{
                    validans = true;
                    textInputAnswer.setError(null);
                }
                enableNext2();
            }
        });


    }

    private void validateEmail() {
        validemail = false;
        if (emailUsernameInput.getText().toString().isEmpty()) {
            textInputEmailUsername.setError("Email cannot be empty");
        }
        else{
            validemail = true;
            enableNext1();
        }
    }

    private void enableNext1(){
        if(validemail){
            next_security.setEnabled(true);
            next_security.setBackgroundResource(R.drawable.login_button_bk);
        } else{
            next_security.setEnabled(false);
            next_security.setBackgroundResource(R.drawable.disable_mode_button);
        }
    }

    private void enableNext2(){
        if(validans){
            reset_pass.setEnabled(true);
            reset_pass.setBackgroundResource(R.drawable.login_button_bk);
        } else{
            reset_pass.setEnabled(false);
            reset_pass.setBackgroundResource(R.drawable.disable_mode_button);
        }
    }

    private void validateSecurityAns(){
        final String endpoint = getString(R.string.url) + "api/users/answer";
        Log.d("Endpoint", endpoint);
        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.POST, endpoint, secqBuild, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("status code", "onResponse: "+statusCode);
                if(statusCode==200) {
                    usrques.setVisibility(View.GONE);
                    back_to_login_layout.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(Forgot_Password.this, "Login Failed : Incorrect credentials" , Toast.LENGTH_SHORT).show();
                    Log.d("Error", "Failing .......");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Forgot_Password.this, "Something went wrong" , Toast.LENGTH_SHORT).show();
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

    private void getSecurityQuestions() {
        final String endpoint = getString(R.string.url) + "api/users/questions?usernameOrEmail="+emailUsernameInput.getText().toString();
        Log.d("Endpoint", endpoint);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    secQues = new ArrayList<JSONObject>();
                    for(int i=0;i<response.length();i++) {
                        JSONObject ques = response.getJSONObject(i);
                        secQues.add(ques);
                    }
                    String ques =secQues.get(0).getString("securityQuestion");
                    security_question.setText(ques);
                    secqBuild.put("securityQuestion", ques);
                    usrname.setVisibility(View.GONE);
                    usrques.setVisibility(View.VISIBLE);
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
