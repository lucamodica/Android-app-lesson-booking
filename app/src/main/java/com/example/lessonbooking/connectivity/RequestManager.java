package com.example.lessonbooking.connectivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lessonbooking.utilities.GenericUtils;

import org.json.JSONObject;

public class RequestManager {

    private final Context ctx;

    private RequestManager(Context ctx){
        this.ctx = ctx;
    }

    public static synchronized RequestManager getInstance(Context ctx){
        Log.d("in RequestManager.getInstance", "New RequestManager " +
                "instance was called");
        return new RequestManager(ctx);
    }


    private void handleError(VolleyError error, String url){
        Toast.makeText(ctx, "Nessuna connessione trovata o " +
                        "errore nei server",
                Toast.LENGTH_LONG).show();
        System.err.println(error.getMessage() + ", url= " + url);
    }

    //Version with a custom error handler
    public void makeRequest(int method,
                            String url,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener){

        Log.d("in RequestManager.makeRequest", "New request " +
                "requested");

        JsonObjectRequest objReq = new JsonObjectRequest(method,
                url, null, listener, errorListener);
        objReq.setTag(GenericUtils.getNetworkTag());

        NetworkSingleton.getInstance(ctx).addToRequestQueue(objReq);
    }

    //Version with the default error handler
    //(that is handleError(VolleyError error, String url))
    public void makeRequest(int method,
                            String url,
                            Response.Listener<JSONObject> listener){

        makeRequest(method, url, listener, error ->
                handleError(error, url));
    }

    //Method to cancell all request in the queue
    public void cancelAllRequests(){

        Log.d("in RequestManager.cancelAllRequests", "Incoming " +
                "RequestQueue reset");

        NetworkSingleton.getInstance(ctx).getRequestQueue().
                cancelAll(GenericUtils.getNetworkTag());
    }
}
