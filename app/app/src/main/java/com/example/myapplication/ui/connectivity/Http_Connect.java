package com.example.myapplication.ui.connectivity;

import android.net.wifi.p2p.WifiP2pManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//re  function callback of success http request
// rel  function   callback of  fail http request
//Get method= 0
//post method 1
public class Http_Connect{
 public  static StringRequest sendReq(int method,String url, Response.Listener<String> re,Response.ErrorListener rel){
    StringRequest  req= new StringRequest(method,url,re, rel);
  return req;

 }

    public static void main(String[] args) {
        Response.Listener<String> re =  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }

        };
        Response.ErrorListener rel= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getCause();
            }
        };

      StringRequest result = sendReq(1,"http://localhost:8080/demo_war_exploded/login?action=auth&account=helo.andrea&password=heheee",re,rel);

    }

}
