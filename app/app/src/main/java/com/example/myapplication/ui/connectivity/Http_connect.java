package com.example.myapplication.ui.connectivity;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

//re  function callback of success http request
// rel  function   callback of  fail http request
//Get method= 0
//post method 1
@SuppressWarnings("serial")
public class Http_connect  {

private JSONObject jsonresult=new JSONObject();

    public Http_connect(JSONObject jsonresult) {
        this.jsonresult = jsonresult;
    }

    public void setJsonresult(JSONObject jsonresult) {
        this.jsonresult = jsonresult;
    }

    public JSONObject getJsonresult() {
        return jsonresult;
    }


    public void SendReq(Context ctx, String url, int method) {
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
                setJsonresult(response);
                System.out.println(getJsonresult().toString());

            }
        } ;

        Response.ErrorListener rel = error -> {
            System.err.println("TomCat non trovato");
            System.err.println(error.toString());
        };

        JsonObjectRequest result = new JsonObjectRequest(method, url, null, re, rel);
        requestQueue.add(result);

    }}





