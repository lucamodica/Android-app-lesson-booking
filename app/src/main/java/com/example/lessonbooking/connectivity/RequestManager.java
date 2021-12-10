package com.example.lessonbooking.connectivity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class RequestManager {

    private Context ctx;

    private RequestManager(Context ctx){
        this.ctx = ctx;
    }

    public static RequestManager getInstance(Context ctx){
        Log.d("in getInstance", "New RequestManager " +
                "instance was called");
        return new RequestManager(ctx);
    }

    public void makeRequest(int method,
                            String url,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener){

        JsonObjectRequest objReq = new JsonObjectRequest(method,
                url, null, listener, errorListener);

        NetworkSingleton.getInstance(ctx).addToRequestQueue(objReq);

    }
}
