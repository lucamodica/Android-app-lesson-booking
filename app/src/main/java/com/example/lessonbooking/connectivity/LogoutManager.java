package com.example.lessonbooking.connectivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.view.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutManager {

    private final Context ctx;
    private final String account;
    private final String role;

    public LogoutManager(Context ctx, String account, String role) {
        this.ctx = ctx;
        this.account = account;
        this.role = role;
    }


    public static synchronized LogoutManager getInstance(Context ctx,
                                                         String account, String role){
        Log.d("in LogoutManager.getInstance", "New RequestManager " +
                "instance was called");
        return new LogoutManager(ctx, account, role);
    }

    private void handleResponse(JSONObject jsonResult){
        try {
            String status = jsonResult.getString("result");
            switch (status) {
                case "success":

                    String toastText = "";
                    if (!role.equals("ospite")){
                        toastText += "Logout di " + account;
                        System.out.println("User logout: " + account +
                                ", with role '" + role + "'");
                    }
                    else{
                        toastText += "Logout dell'ospite";
                        System.out.println("Guest logout");
                    }

                    //Intent to take the user back to LoginActivity
                    Toast.makeText(ctx, toastText + " avvenuto " +
                            "con successo", Toast.LENGTH_LONG).show();
                    break;

                case "no_user":
                    Toast.makeText(ctx, ctx.getString(R.string.no_user_result) +
                            " per effettuare logout", Toast.LENGTH_LONG).show();
                    break;
            }

            ctx.startActivity(new Intent(ctx,
                    LoginActivity.class));
            ((Activity) ctx).finish();
        }
        catch (JSONException ed) {
            ed.printStackTrace();
        }
    }

    public void makeLogout(){

        String url = ctx.getString(R.string.servlet_url) + "logout";

        RequestManager.getInstance(ctx).makeRequest(Request.Method.GET,
                url, this::handleResponse
        );
    }
}
