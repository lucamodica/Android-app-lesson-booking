package com.example.lessonbooking.connectivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class RequestManager {

    private final Context ctx;

    private RequestManager(Context ctx){
        this.ctx = ctx;
    }

    public static RequestManager getInstance(Context ctx){
        Log.d("in getInstance", "New RequestManager " +
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

        JsonObjectRequest objReq = new JsonObjectRequest(method,
                url, null, listener, errorListener);

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
}
