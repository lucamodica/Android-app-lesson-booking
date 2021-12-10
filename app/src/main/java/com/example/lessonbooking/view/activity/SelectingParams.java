package com.example.lessonbooking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.lessonbooking.R;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lessonbooking.connectivity.NetworkSingleton;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.model.Course;
import com.example.lessonbooking.model.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectingParams extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    private static JSONObject course, teacher;
    Context ctx;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingparams);

        course = new JSONObject();
        teacher = new JSONObject();

        ctx = getApplicationContext();

        url = getString(R.string.servlet_url) + "selectTable?objType=";
        Bundle b = getIntent().getExtras();

        if (b.containsKey("account") && b.containsKey("role")){
            String account = getIntent().getStringExtra("account");
            String role = getIntent().getStringExtra("role");
            Toast.makeText(ctx, "Hello, " + account + "!, role: " + role,
                    Toast.LENGTH_LONG).show();
        }
        else {
            finish();
        }
    }

    protected void onStart() {
        super.onStart();

        //Fetch data
        fetchData("corso");

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void fetchData(String objType){
        String urlReq = url + objType;

        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, urlReq,
                response -> {
                    System.out.println(response);
                    if (objType.equals("corso")){
                        course = response;
                        fetchData("docente");
                    }
                    else if (objType.equals("docente")) {
                        teacher = response;
                        createDropdowns();
                    }
                },
                error -> {
                    Toast.makeText(ctx, error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    System.err.println(error.getMessage() + ", url= " + url);
                }
                );
    }

    private void createDropdowns(){
        //Arraylist for adapters
        ArrayList<String> courseslist = new ArrayList<>();
        ArrayList<String> teacherlist = new ArrayList<>();
        JSONArray arr;

        try {
            if (course.getString("result").equals("success")) {
                arr = course.getJSONArray("content");
                for (int i = 0; i < arr.length(); i++) {
                    courseslist.add(arr.getJSONObject(i).getString("title"));
                }
                System.out.println(courseslist.toString());

                //Init dropdown
                Spinner corsi = findViewById(R.id.courses);
                corsi.setOnItemSelectedListener(this);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        courseslist);
                corsi.setAdapter(adapter);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (teacher.getString("result").equals("success")) {
                arr = teacher.getJSONArray("content");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject teacher = arr.getJSONObject(i);
                    String teacherStr = teacher.getString("name") + " " +
                            teacher.getString("surname") + " (" +
                            teacher.getString("id_number") + ")";
                    teacherlist.add(teacherStr);
                }
                System.out.println(teacherlist.toString());

                Spinner teacher = findViewById(R.id.teachers);
                teacher.setOnItemSelectedListener(this);
                ArrayAdapter<String> adaptert = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        teacherlist);
                teacher.setAdapter(adaptert);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

}





