package com.example.myapplication.ui.connectivity;


import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//re  function callback of success http request
// rel  function   callback of  fail http request
//Get method= 0
//post method 1
public class Http_connect {
    public static JsonObjectRequest sendReq(String url, Response.Listener<JSONObject> re, Response.ErrorListener rel) {

        return new JsonObjectRequest(Request.Method.POST, url, null, re, rel);



    }

    public Http_connect() {
    }

    public void mainv(Context ctx) {

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

        Response.Listener<JSONObject> re = response -> {
            //System.out.printf("" + response.toString());
            try {
                if (response != null) {
                    String d = response.getString("result")+" ";
                    JSONObject user = response.getJSONObject("user");
                    d=d+user.getString("account")+" "+user.getString("role")+" ";
                    System.out.println(d);




                }
            } catch (JSONException e) {
                System.out.println("Errore: " + e.toString());
            }
        };

        Response.ErrorListener rel = error -> {

            System.err.println(error.getCause());
        };

        JsonObjectRequest result = sendReq("http://10.0.2.2:8080/demo_war_exploded/login?action=auth&account=helo.andrea&password=heheee", re, rel);
        requestQueue.add(result);
    }


}
