package com.example.lessonbooking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.lessonbooking.R;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lessonbooking.connectivity.NetworkSingleton;
import com.example.lessonbooking.model.Course;
import com.example.lessonbooking.model.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.webkit.CookieManager;

import java.util.ArrayList;


public class SelectingParams extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    private static JSONObject course, teacher;
    Context ctx;
    private String url;
    private String account;
    private String role;
    private String jsessionid;
    final private String[] params = {"corso", "docente"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingparams);
        course = new JSONObject();
        teacher = new JSONObject();

        ctx = getApplicationContext();

        url = getString(R.string.servlet_url) + "selectTable?objType=";
        Bundle b = getIntent().getExtras();

        if (b.containsKey("account") && b.containsKey("role") &&
            b.containsKey("jsessionid")){
            account = getIntent().getStringExtra("account");
            role = getIntent().getStringExtra("role");
            jsessionid = getIntent().getStringExtra("jsessionid");
        }
        else {
            finish();
        }
    }


    private void fetchData(String objType){
        String urlReq = url + objType;
        JsonObjectRequest jsonReq = new JsonObjectRequest(
                Request.Method.GET,
                urlReq,
                null,
                response -> {
                    if (objType.equals("corso")){
                        course = response;
                    }
                    else if (objType.equals("docente")) {
                        teacher = response;
                    }
                },
                error -> {
                    Toast.makeText(ctx, error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    System.err.println(error.getMessage() + ", url= " + url);
                }
        );

        NetworkSingleton.getInstance(ctx).addToRequestQueue(jsonReq);
        if (objType.equals("corso")) {
            fetchData("docente");
        }
    }

    protected void onStart() {
        super.onStart();

        CookieManager manager = CookieManager.getInstance();
        manager.setCookie("JSESSIONID", jsessionid);
        //CookieHandler.getDefault().put();

        //Arraylist for adapters
        ArrayList<Course> courseslist = new ArrayList<>();
        ArrayList<Teacher> teacherlist = new ArrayList<>();

        //Fetch data
        fetchData("corso");

        if(getCourse().has( "result")){
            try {
                if (getCourse().getString("result").equals("success")) {
                    JSONArray arr = course.getJSONArray("content");
                    for (int i = 0; i < arr.length(); i++) {
                        Course c = new Course(arr.getJSONObject(i).getString("title"),
                                arr.getJSONObject(i).getString("desc"));
                        courseslist.add(c);
                    }
                    System.out.println(courseslist.toString());
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            //riempio arraylist con i campi che mi servono
            try {
                if (teacher.getString("result").equals("success")) {
                    JSONArray arr1= course.getJSONArray("content");
                    for (int i = 0; i < arr1.length(); i++) {
                        Teacher ta = new Teacher(
                                arr1.getJSONObject(i).getString("name"),
                                arr1.getJSONObject(i).getString("surname"),
                                arr1.getJSONObject(i).getString("id_number")
                        );
                        teacherlist.add(ta);
                    }
                    //inizializzo dropdown
                    Spinner corsi =  findViewById(R.id.corsi);
                    corsi.setOnItemSelectedListener(this);
                    ArrayAdapter<Course> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_dropdown_item,
                            courseslist);
                    corsi.setAdapter(adapter);

                    Spinner teacher = findViewById(R.id.docenti);
                    teacher.setOnItemSelectedListener(this);
                    ArrayAdapter<Teacher> adaptert = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_dropdown_item,
                            teacherlist);
                    teacher.setAdapter(adaptert);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            System.out.println("richiesta fallita");
        }

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

}





