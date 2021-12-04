package com.example.myapplication.ui.connectivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
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
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.ui.connectivity.NetworkActivity;
import com.example.myapplication.ui.model.Course;
import com.example.myapplication.ui.model.Teacher;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.crypto.Cipher;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class selectingparams extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
 private static JSONObject course,teacher;
 private static JSONObject auth= new JSONObject();

 Context ctx;
 final private String url="http://10.0.2.2:8080/demo_war_exploded/selectTable?objType=";
  final private String[] params={"corso","docente","auth"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingparams);
        course = new JSONObject();
        teacher= new JSONObject();

        ctx=getApplicationContext();

       }




    public void SendReq(Context ctx, String url, int method,String params) {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.start();
        Response.Listener<JSONObject> re=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               if(params=="corso"){

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


          JsonObjectRequest result =new JsonObjectRequest(method, url, null, re, rel);
        requestQueue.add(result);

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
    protected void onStart() {

        super.onStart();
        Runnable e = new Runnable() {
            @Override
            public void run() {
                SendReq(ctx,"http://10.0.2.2:8080/demo_war_exploded/login?action=auth&account=hi.marco&password=marco",1,params[2]);
           try{
               System.out.println("inizio");
               Thread.sleep(5000);
               System.out.println("finito");
            }catch(InterruptedException I){

           }
            }
        };
        Thread t= new Thread(e);
        t.start();

       try {
           t.join();
       }catch(InterruptedException es){
           System.out.println(es.toString());
       }

        try {
            if(getAuth().has("result")){

                String status= getAuth().getString("result");

                System.out.println(status);
                if(status.equals("success")) {
                    System.out.println("ciao");
                    Runnable r= new Runnable() {
                        @Override
                        public void run() {
                            SendReq(selectingparams.this, url + params[0], 0, params[0]);
                            try{
                                System.out.println("inizio"+Thread.currentThread().getName());
                                    Thread.sleep(5000);
                                System.out.println("fine"+Thread.currentThread().getName());
                            }catch(InterruptedException ed1){

                            }
                        }
                    };
                    Thread t2=new Thread(r);
                    t2.start();
                    try {
                        t2.join();
                    }catch(InterruptedException ed1){
                        System.out.println(ed1.toString());
                    }
                    ArrayList<Course> courseslist = new ArrayList<>();
                    ArrayList<Teacher> teacherlist = new ArrayList<>();
                    System.out.println(url + params[0]);
                    if(getCourse().has( "result")){
                        if (getCourse().getString("result").equals("success")) {
                        JSONArray arr = course.getJSONArray("content");
                        for (int i = 0; i < arr.length(); i++) {
                            Course c = new Course(arr.getJSONObject(i).getJSONObject("title").toString(), arr.getJSONObject(i).getJSONObject("desc").toString());
                            courseslist.add(c);
                        }
                            System.out.println(courseslist.toString());
                        }
                        SendReq(selectingparams.this, url + params[1], 0, params[1]);
                        if (teacher.getString("result").equals("success")) {
                            JSONArray    arr1= course.getJSONArray("content");
                            for (int i = 0; i < arr1.length(); i++) {
                                Teacher ta = new Teacher(arr1.getJSONObject(i).getJSONObject("name").toString(), arr1.getJSONObject(i).getJSONObject("surname").toString(), arr1.getJSONObject(i).getJSONObject("id_number").toString());
                                teacherlist.add(ta);
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

                    } else {
                        System.out.println("errore");
                    }

                }  }
        }catch(JSONException ed1){
            System.out.println(ed1.toString());
        }
    }



}





