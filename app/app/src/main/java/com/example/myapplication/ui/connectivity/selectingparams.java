package com.example.myapplication.ui.connectivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.R;
import com.example.myapplication.ui.connectivity.NetworkActivity;
import com.example.myapplication.ui.model.Course;
import com.example.myapplication.ui.model.Teacher;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class selectingparams extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
 private JSONObject course,teacher,auth;
 private String url="http://10.0.2.2:8080/demo_war_exploded/selectTable?objType=";
 private String[] params={"corso","docente","auth"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingparams);
        course = new JSONObject();
        teacher= new JSONObject();
        RequestQueue requestQueue;
        NetworkActivity objNetworkActivity = new NetworkActivity(selectingparams.this);
        Cache cache = objNetworkActivity.cache;  // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start(); requestQueue.add(SendReq(selectingparams.this,url+params[0],0,params[0]));
        requestQueue.add(SendReq(selectingparams.this,"http://10.0.2.2:8080/demo_war_exploded/login?action=auth&account=hi.marco&password=marco",1,params[2]));

        ArrayList<Course> courseslist=new ArrayList<>();
        ArrayList<Teacher> teacherlist=new ArrayList<>();
        System.out.println(url+params[0]);
        System.out.println();      requestQueue.add(SendReq(selectingparams.this,url+params[0],0,params[0]));
        try{
        if(course.getString("result")=="success"){
         JSONArray arr =course.getJSONArray("content");
            for (int i=0;i<arr.length();i++) {
                Course c= new Course(arr.getJSONObject(i).getJSONObject("title").toString(),arr.getJSONObject(i).getJSONObject("desc").toString());
          courseslist.add(c);
           }
            requestQueue.add(SendReq(selectingparams.this,url+params[1],0,params[1]));
            if(teacher.getString("result")=="success") {
                arr = course.getJSONArray("content");
                for (int i = 0; i < arr.length(); i++) {
                   Teacher t = new Teacher(arr.getJSONObject(i).getJSONObject("name").toString(), arr.getJSONObject(i).getJSONObject("surname").toString(),arr.getJSONObject(i).getJSONObject("id_number").toString());
                   teacherlist.add(t);
                }

                Spinner corsi = (Spinner) findViewById(R.id.corsi);
                corsi.setOnItemSelectedListener(this);
                ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_dropdown_item, courseslist);
                corsi.setAdapter(adapter);

                Spinner teacher = (Spinner) findViewById(R.id.docenti);
                teacher.setOnItemSelectedListener(this);
                ArrayAdapter<Teacher> adaptert = new ArrayAdapter<Teacher>(this, android.R.layout.simple_spinner_dropdown_item, teacherlist);
                teacher.setAdapter(adaptert);
            }

        }else{
            System.out.println("errore");
        }
        }catch(JSONException ed){
            System.out.println(ed.toString());
        }

    }

    public  JsonObjectRequest SendReq(Context ctx, String url, int method,String params) {


        Response.Listener<JSONObject> re=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               if(params=="corso"){
                   System.out.println("ciao");
                   setCourse(response);
                   System.out.println(getCourse().toString());
               }else{ if(params =="docente"){
                   setTeacher(response);
                   System.out.println(getTeacher().toString());
               }else{if(params=="auth"){
                   setAuth(response);
                   System.out.println(getAuth().toString());
               }

               }

               }

            }
        } ;

        Response.ErrorListener rel = error -> {
            System.err.println("TomCat non trovato");
            System.err.println(error.toString());
        };

     return    new JsonObjectRequest(method, url, null, re, rel);


    }

    public JSONObject getCourse() {
        return course;
    }

    public void setCourse(JSONObject course) {
        this.course = course;
    }

    public JSONObject getTeacher() {
        return teacher;
    }

    public void setTeacher(JSONObject teacher) {
        this.teacher = teacher;
    }

    public JSONObject getAuth() {
        return auth;
    }

    public void setAuth(JSONObject auth) {
        this.auth = auth;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}