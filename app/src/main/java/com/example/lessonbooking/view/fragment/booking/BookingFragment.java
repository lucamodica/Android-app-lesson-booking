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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.SelectCourseTeacherBinding;
import com.example.lessonbooking.model.Course;
import com.example.lessonbooking.model.Teacher;
import com.example.lessonbooking.view.activity.LoginActivity;
import com.example.lessonbooking.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class BookingFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private BookingViewModel bookingViewModel;
    private SelectCourseTeacherBinding binding;
    private View root;
    private Context ctx;
    private String account, role;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        root.findViewById(R.id.spinner_courses).
                setVisibility(View.GONE);
        fetchData("corso");
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
    private void onBackPressed(){

    }
    private void onNextPressed(){

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
        String urlReq = getString(R.string.servlet_url) + "selectTable?objType=" +
                objType;

        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, urlReq,
                success -> handleResponse(success, objType)
        );
    }
    private void createDropdown(JSONArray arr, String objType)
            throws JSONException {

        ArrayList<String> list = new ArrayList<>();
        Object elem;

        for (int i = 0; i < arr.length(); i++) {
            elem = (objType.equals("corso")) ? new Course(arr.getJSONObject(i))
                    : new Teacher(arr.getJSONObject(i));
            list.add(elem.toString());
        }
        System.out.println(list.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,
                android.R.layout.simple_spinner_dropdown_item, list);
        Spinner spinner = requireView().findViewById(
                (objType.equals("corso")) ? R.id.spinner_courses
                        : R.id.spinner_teachers
        );
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
    }
    private void handleResponse(JSONObject obj, String objType){

        try {
            String result = obj.getString("result");
            switch (result) {
                case "success":
                    if (objType.equals("docente") || objType.equals("corso")){
                        createDropdown(obj.getJSONArray("content"), objType);
                        root.findViewById((objType.equals("corso")) ? R.id.spinner_courses
                                : R.id.spinner_teachers).
                                setVisibility(View.VISIBLE);
                    }
                    else{
                        System.out.println("Bookable slots case (to be done)");
                    }
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