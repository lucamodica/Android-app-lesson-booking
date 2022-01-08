package com.example.lessonbooking.connectivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lessonbooking.R;
import com.example.lessonbooking.utilities.SuccessHandler;
import com.example.lessonbooking.view.activity.LoginActivity;

import org.json.JSONException;
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
    private void handleResponse(JSONObject obj, SuccessHandler
            successHandler){
        try {
            String result = obj.getString("result");
            switch (result){
                case "success":
                    successHandler.handle(obj);
                    break;
                case "no_user":
                    Toast.makeText(ctx, R.string.no_user_result,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ctx, LoginActivity.class);
                    ctx.startActivity(i);
                    break;

                case "invalid_object":
                    Toast.makeText(ctx, R.string.invalid_object_result,
                            Toast.LENGTH_LONG).show();
                    break;

                case "not_allowed":
                    Toast.makeText(ctx, R.string.not_allowed_result,
                            Toast.LENGTH_LONG).show();
                    break;

                case "params_null":
                    Toast.makeText(ctx, R.string.params_null_result,
                            Toast.LENGTH_LONG).show();
                    break;

                case "query_failed":
                    Toast.makeText(ctx, R.string.query_failed_result,
                            Toast.LENGTH_LONG).show();
                    break;


                //Specific case for an insert request
                case "invalid_checks":
                    Toast.makeText(ctx, R.string.invalid_checks_result,
                            Toast.LENGTH_LONG).show();
                    break;

                case "invalid_course_teacher_for_lesson":
                    Toast.makeText(ctx, R.string.invalid_course_teacher_for_lesson_result,
                            Toast.LENGTH_LONG).show();
                    break;

                case "slot_busy":
                    Toast.makeText(ctx, R.string.slot_busy_result,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (IllegalStateException | JSONException e) {
            e.printStackTrace();
        }
    }


    //Version with a custom error handler
    public void makeRequest(int method,
                            String url,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener){

        JsonObjectRequest objReq = new JsonObjectRequest(method,
                url, null, listener, errorListener);
        objReq.setTag(R.string.network_tag);

        cancelAllRequests();
        Log.d("in RequestManager.makeRequest", "New request " +
                "requested");
        NetworkSingleton.getInstance(ctx).addToRequestQueue(objReq);
    }

    //Version with the default error handler
    //(that is handleError(VolleyError error, String url))
    public void makeRequest(int method, String url,
                            Response.Listener<JSONObject> listener){

        makeRequest(method, url,
                listener,
                error -> handleError(error, url));
    }

    //Version with the default error and response handler
    //(the only thing to specify is how the "success" case
    // have to be handled)
    public void makeRequest(int method, String url,
                            SuccessHandler successHandler){

        makeRequest(method, url,
                success -> handleResponse(success, successHandler),
                error -> handleError(error, url));
    }

    //Method to cancell all request in the queue
    public void cancelAllRequests(){

        Log.d("in RequestManager.cancelAllRequests", "Incoming " +
                "RequestQueue reset");

        NetworkSingleton.getInstance(ctx).cancelAll();
    }
}
