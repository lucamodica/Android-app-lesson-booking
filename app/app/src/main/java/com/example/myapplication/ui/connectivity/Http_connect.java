package com.example.myapplication.ui.connectivity;


import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.MainActivity;
import com.example.myapplication.login;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

//re  function callback of success http request
// rel  function   callback of  fail http request
//Get method= 0
//post method 1
@SuppressWarnings("serial")
public class Http_connect implements Parcelable {

private JSONObject jsonresult=null;

public Http_connect(){}
    protected Http_connect(Parcel in) {
    }

    public static final Creator<Http_connect> CREATOR = new Creator<Http_connect>() {
        @Override
        public Http_connect createFromParcel(Parcel in) {
            return new Http_connect(in);
        }

        @Override
        public Http_connect[] newArray(int size) {
            return new Http_connect[size];
        }
    };

    public void setJsonresult(JSONObject jsonresult) {
        this.jsonresult = jsonresult;
    }

    public JSONObject getJsonresult() {
        return jsonresult;
    }


    public static    void SendReq(Context ctx, String url, int method,Http_connect http) {
        RequestQueue requestQueue;
        NetworkActivity objNetworkActivity = new NetworkActivity(ctx);


// Instantiate the cache
        Cache cache = objNetworkActivity.cache;  // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        Response.Listener<JSONObject> re=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             http.setJsonresult(response);
                System.out.println(http.getJsonresult().toString());
                    //Intent i = new Intent(MainActivity.this,login.class);
                    //i.putExtra("retieve",http);

            }
        } ;

        Response.ErrorListener rel = error -> {

            System.err.println(error.getCause());
        };

        JsonObjectRequest result = new JsonObjectRequest(method, url, null, re, rel);
        requestQueue.add(result);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
