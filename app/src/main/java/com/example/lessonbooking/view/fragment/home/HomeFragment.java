package com.example.lessonbooking.view.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.adapter.LessonsRecyclerViewAdapter;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.FragmentHomeBinding;
import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.view.activity.LoginActivity;
import com.example.lessonbooking.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    //ViewModel and main fragment vars
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Context ctx;
    View root;

    //Recycler, user info
    LessonsRecyclerViewAdapter adapter;
    String account, role;
    List<Lesson> lessons;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Context and ViewModel setup
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container,
                false);
        root = binding.getRoot();
        ctx = root.getContext();

        //Get params from Intent and setting it
        role = ((MainActivity) requireActivity()).getRole();
        ((TextView) root.findViewById(R.id.role_text)).setText("Ruolo: " + role);
        if (role.equals("utente") || role.equals("amministratore")){
            account = ((MainActivity) requireActivity()).getAccount();
            ((TextView) root.findViewById(R.id.account_text)).
                    setText("Nome utente: " + account);
        }

        //Setting the logout button
        root.findViewById(R.id.logout_btn).setOnClickListener(v -> logout());

        //If the user logged is a guest, the login suggest
        //will be showed
        if (role.equals("ospite")){
            showLoginSuggest();
        }

        return root;
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable
            Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (role.equals("utente") || role.equals("amministratore")){
            //Fetch lessons
            fetchLessons();

            ActivityResultLauncher<Intent> lessonInfoLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        Intent data = result.getData();
                        if (data != null && data.hasExtra("lessonStatus") &&
                                data.hasExtra("lessonIndex")){

                            lessons.get(data.getIntExtra("lessonIndex", -1)).
                                    setStatus(data.getStringExtra("lessonStatus"));
                            homeViewModel.setLessons(lessons);
                        }
                        else {
                            Toast.makeText(ctx,
                                    "Parametri mancanti nell'Intent!",
                                    Toast.LENGTH_LONG).show();
                            requireActivity().finish();
                        }
                    }
            );

            //RecyclerView and adapter setup
            RecyclerView recyclerView = requireView().findViewById(R.id.RecyclerLessons);
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            adapter = new LessonsRecyclerViewAdapter(ctx, new ArrayList<>(),
                    lessonInfoLauncher);
            recyclerView.setAdapter(adapter);

            //ViewModel binding setup
            homeViewModel.getLessons().observe(getViewLifecycleOwner(),
                    lessonsChanged -> {
                        adapter.notifyDataSetChanged();
                        adapter.setData(lessonsChanged);
                    }
            );
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void showLoginSuggest(){

        ((TextView) root.findViewById(R.id.waiting_home)).
                setText("");

        root.findViewById(R.id.suggest_login_home_layout).
                setVisibility(View.VISIBLE);

        root.findViewById(R.id.suggest_login_home_btn).
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

    private void fetchLessons(){
        String url = getString(R.string.servlet_url) + "selectElems?" +
                "objType=ripetizione&user=" + account;

        RequestManager.getInstance(ctx).cancelAllRequests();
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                this::handleFetchLessonResponse
        );
    }
    private void setupCatalogView(JSONObject obj){

        //Setting up the hashmap for the fetched catalog
        lessons = new ArrayList<>();

        try {

            //Remove the waiting warning
            ((TextView) root.findViewById(R.id.waiting_home)).
                    setText("");

            //Fill the HashMap with the fetched slots
            JSONArray arrLessons = obj.getJSONArray("content");
            for (int i = 0; i < arrLessons.length(); i++) {
                JSONObject lesson = arrLessons.getJSONObject(i);
                lessons.add(new Lesson(
                        lesson.getString("teacher"),
                        lesson.getString("course"),
                        lesson.getString("t_slot"),
                        lesson.getString("day"),
                        lesson.getString("user"),
                        lesson.getString("status"),
                        lesson.getString("name"),
                        lesson.getString("surname")
                ));
            }

            homeViewModel.setLessons(lessons);
        }
        catch (IllegalStateException | JSONException e){
            System.out.println(e.getMessage());
        }

    }
    private void handleFetchLessonResponse(JSONObject jsonResult){

        try {
            String result = jsonResult.getString("result");
            switch (result) {
                case "success":
                    setupCatalogView(jsonResult);
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