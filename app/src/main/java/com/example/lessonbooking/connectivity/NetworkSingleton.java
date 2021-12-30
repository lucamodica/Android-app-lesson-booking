package com.example.lessonbooking.connectivity;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.lessonbooking.utilities.GenericUtils;

public class NetworkSingleton {

    private RequestQueue requestQueue;
    private final Context ctx;

    private NetworkSingleton(Context ctx) {
        this.ctx = ctx;
        requestQueue = getRequestQueue();
    }

    static synchronized NetworkSingleton getInstance(Context context) {
        Log.d("in getInstance", "New NetworkSingleton " +
                "instance was called");

        return new NetworkSingleton(context);
    }

    RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    void cancelAll(){
        getRequestQueue().cancelAll(GenericUtils.getNetworkTag());
        getRequestQueue().stop();
        getRequestQueue().start();
    }

}