package com.example.lessonbooking.view.fragment.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.FragmentBookingBinding;
import com.example.lessonbooking.view.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class BookingFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private BookingViewModel bookingViewModel;
    private FragmentBookingBinding binding;
    private JSONObject JSONcourse, JSONteacher;
    Context ctx;
    private String url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Setting up ViewModel (and refered layout) of bookable lesson slots
        //(TO BE MOVED)
        bookingViewModel =
                new ViewModelProvider(this).get(BookingViewModel.class);
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Init setting
        View view = inflater.inflate(R.layout.select_course, container,
                false);
        ctx = view.getContext();
        JSONcourse = new JSONObject();
        JSONteacher = new JSONObject();
        url = getString(R.string.servlet_url) + "selectTable?objType=";

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                response -> Log.d("JSONResponse",
                        String.valueOf(response))
        );
    }

    /*
    private void createDropdowns(){
        //Arraylist for adapters
        JSONArray arr;

        try {
            if (JSONcourse.getString("result").equals("success")) {
                ArrayList<String> courseslist = new ArrayList<>();
                arr = JSONcourse.getJSONArray("content");
                for (int i = 0; i < arr.length(); i++) {
                    courseslist.add(arr.getJSONObject(i).getString("title"));
                }
                System.out.println(courseslist.toString());

                //Init dropdown
                Spinner courses = requireView().findViewById(R.id.courses);
                courses.setOnItemSelectedListener(this);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,
                        android.R.layout.simple_spinner_dropdown_item, courseslist);
                courses.setAdapter(adapter);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (JSONteacher.getString("result").equals("success")) {
                ArrayList<String> teacherlist = new ArrayList<>();
                arr = JSONteacher.getJSONArray("content");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject teacher = arr.getJSONObject(i);
                    String teacherStr = teacher.getString("name") + " " +
                            teacher.getString("surname") + " (" +
                            teacher.getString("id_number") + ")";
                    teacherlist.add(teacherStr);
                }
                System.out.println(teacherlist.toString());

                Spinner teacher = requireView().findViewById(R.id.teachers);
                teacher.setOnItemSelectedListener(this);
                ArrayAdapter<String> adaptert = new ArrayAdapter<>(ctx,
                        android.R.layout.simple_spinner_dropdown_item,
                        teacherlist);
                teacher.setAdapter(adaptert);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

     */
    /*TODO ELIMINARE*/void createDropdowns(){}
    private void handleCatalogResponse(JSONObject obj){
        try {
            String result = obj.getString("result");
            switch (result) {
                case "success":
                    createDropdowns();
                    break;

                case "no_user":
                    Toast.makeText(ctx, R.string.no_user_result,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ctx, LoginActivity.class);
                    startActivity(i);
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
            }
        } catch (IllegalStateException | JSONException e) {
            e.printStackTrace();
        }

    }

}