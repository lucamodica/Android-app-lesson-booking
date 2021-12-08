package com.example.lessonbooking.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lessonbooking.R;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lessonbooking.model.Course;
import com.example.lessonbooking.model.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class selectingparams extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
 private static JSONObject course,teacher;
 private static JSONObject auth= new JSONObject();

 Context ctx;
 final private String[] url={"http://10.0.2.2:8080/demo_war_exploded/login?action=auth&account=hi.marco&password=marco","http://10.0.2.2:8080/demo_war_exploded/selectTable?objType="};
  final private String[] params={"corso","docente","auth"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingparams);
        course = new JSONObject();
        teacher= new JSONObject();

        ctx=getApplicationContext();

       }
    protected void onStart() {
        super.onStart();
        // richiesta login
        Runnable e = () -> {
            SendReq(ctx,url[0],1,params[2]);
            try{
                System.out.println("inizio"+Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("finito"+Thread.currentThread().getName());
            }catch(InterruptedException I){
                System.out.println("Thread interrotto"+Thread.currentThread().getName());
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
                    //richiesta corsi
                    Runnable r= () -> {
                        SendReq(selectingparams.this, url[1] + params[0], 0, params[0]);
                        try{
                            System.out.println("inizio"+Thread.currentThread().getName());
                            Thread.sleep(5000);
                            System.out.println("fine"+Thread.currentThread().getName());
                        }catch(InterruptedException ed1){
                            System.out.println("Thread interrotto"+Thread.currentThread().getName());

                        }
                    };
                    Thread t2=new Thread(r);
                    t2.start();
                    try {
                        t2.join();
                    }catch(InterruptedException ed1){
                        System.out.println(ed1.toString());
                    }
                    //Arraylist per gli adapters
                    ArrayList<Course> courseslist = new ArrayList<>();
                    ArrayList<Teacher> teacherlist = new ArrayList<>();


                    System.out.println(url[1] + params[0]);

                    if(getCourse().has( "result")){//serve per evitare eccezioni

                        if (getCourse().getString("result").equals("success")) {
                            JSONArray arr = course.getJSONArray("content");
                            for (int i = 0; i < arr.length(); i++) {
                                //campi necessari per riempre arraylist
                                Course c = new Course(arr.getJSONObject(i).getJSONObject("title").toString(), arr.getJSONObject(i).getJSONObject("desc").toString());
                                courseslist.add(c);
                            }
                            System.out.println(courseslist.toString());
                        }

                        // richiesta docenti
                        r= () -> {
                            SendReq(selectingparams.this, url[1] + params[1], 0, params[1]);                            try{
                                System.out.println("inizio"+Thread.currentThread().getName());
                                Thread.sleep(5000);
                                System.out.println("fine"+Thread.currentThread().getName());
                            }catch(InterruptedException ed1){
                                System.out.println("Thread interrotto"+Thread.currentThread().getName());

                            }
                        };
                        Thread t3=new Thread(r);
                        t3.start();
                        try {
                            t3.join();
                        }catch(InterruptedException ed1){
                            System.out.println(ed1.toString());
                        }
                        //riempio arraylist con i campi che mi servono
                        if (teacher.getString("result").equals("success")) {
                            JSONArray    arr1= course.getJSONArray("content");
                            for (int i = 0; i < arr1.length(); i++) {
                                Teacher ta = new Teacher(arr1.getJSONObject(i).getJSONObject("name").toString(), arr1.getJSONObject(i).getJSONObject("surname").toString(), arr1.getJSONObject(i).getJSONObject("id_number").toString());
                                teacherlist.add(ta);
                            }
                            //inizializzo dropdown
                            Spinner corsi =  findViewById(R.id.corsi);
                            corsi.setOnItemSelectedListener(this);
                            ArrayAdapter<Course> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseslist);
                            corsi.setAdapter(adapter);

                            Spinner teacher = findViewById(R.id.docenti);
                            teacher.setOnItemSelectedListener(this);
                            ArrayAdapter<Teacher> adaptert = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teacherlist);
                            teacher.setAdapter(adaptert);
                        }

                    } else {
                        System.out.println(" richiesta fallita");
                    }

                }  }
        }catch(JSONException ed1){
            System.out.println(ed1.toString());
        }
    }

    public void SendReq(Context ctx, String url, int method,String params) {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.start();
        Response.Listener<JSONObject> re= response -> {
           if(params.equals("corso")){
               setCourse(response);
               System.out.println(getCourse().toString());
           }else{
               if(params.equals("docente")){
               setTeacher(response);
               System.out.println(getTeacher().toString());
           }else{
               if(params.equals("auth")){
               setAuth(response);
               System.out.println(getAuth().toString());
           }

           }

           }

        };
        Response.ErrorListener rel = error -> {
            System.err.println("TomCat non trovato");
            System.err.println(error.toString());
        };
        JsonObjectRequest result =new JsonObjectRequest(method, url, null, re, rel);
        requestQueue.add(result);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

}





