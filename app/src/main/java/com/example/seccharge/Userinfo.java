package com.example.seccharge;

import android.content.Context;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class Userinfo {
    private String token;
    private Long userId;
    private boolean emailVerified;
    private boolean introOpened = false;
    private int statusCode = 0;

    public Userinfo(String token, Long userId, boolean emailVerified, boolean introOpened) {
        this.token = token;
        this.userId = userId;
        this.emailVerified = emailVerified;
        this.introOpened = introOpened;
    }

    public String getToken() { return token; }
    public void setToken(String token) {
        this.token = token;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public boolean getEmailVerified() {
        return emailVerified;
    }
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    public boolean isIntroOpened() { return introOpened; }
    public void setIntroOpened(boolean introOpened) { this.introOpened = introOpened; }

    public void refreshToken(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        String endPoint =   context.getString(R.string.url)+"api/users/refreshToken";
        HashMap<String, String> data = new HashMap<>();
        data.put("token", getToken());
        final JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.POST, endPoint, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    if(statusCode==200) {
                        setToken(response.getString("token"));
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
}
