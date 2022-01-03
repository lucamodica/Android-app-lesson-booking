package com.example.lessonbooking.view.fragment.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.SelectCourseTeacherBinding;
import com.example.lessonbooking.view.activity.LoginActivity;
import com.example.lessonbooking.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class BookingFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private BookingViewModel bookingViewModel;
    private SelectCourseTeacherBinding binding;
    private View root;
    private Context ctx;

    private JSONObject JSONcourse, JSONteacher;
    private String url, account, role;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Context and ViewModel setup
        bookingViewModel =
                new ViewModelProvider(this).get(BookingViewModel.class);
        binding = SelectCourseTeacherBinding.inflate(inflater, container,
                false);
        root = binding.getRoot();
        ctx = root.getContext();


        //Get params from Intent and setting it
        role = ((MainActivity) requireActivity()).getRole();
        if (role.equals("utente") || role.equals("amministratore")){
            account = ((MainActivity) requireActivity()).getAccount();
        }

        //If the user logged is a guest, the login suggest
        //will be showed
        if (role.equals("ospite")){
            showLoginSuggest();
        }

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void showLoginSuggest(){

        root.findViewById(R.id.select_course_layout).
                setVisibility(View.GONE);
        root.findViewById(R.id.select_teacher_layout).
                setVisibility(View.GONE);
        root.findViewById(R.id.next_back_btns).
                setVisibility(View.GONE);

        root.findViewById(R.id.suggest_login_booking_layout).
                setVisibility(View.VISIBLE);
        root.findViewById(R.id.suggest_login_booking_btn).
                setOnClickListener(v -> logout());
    }

    private void logout(){
        String url = getString(R.string.servlet_url) +
                "logout";

        RequestManager.getInstance(ctx).makeRequest(Request.Method.GET,
                url, this::handleLogoutResponse
        );
    }
    private void handleLogoutResponse(JSONObject jsonResult){
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
                    Toast.makeText(ctx, getString(R.string.no_user_result) + " per " +
                            "effettuare logout", Toast.LENGTH_LONG).show();
                    break;
            }

            requireActivity().startActivity(new Intent(ctx,
                    LoginActivity.class));
            requireActivity().finish();
        }
        catch (JSONException ed) {
            ed.printStackTrace();
        }
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