package com.example.lessonbooking.connectivity;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkSingleton {

    private RequestQueue requestQueue;
    private final Context ctx;

    private NetworkSingleton(Context ctx) {
        this.ctx = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        Log.d("in getInstance", "New NetworkSingleton " +
                "instance was called");

        return new NetworkSingleton(context);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}