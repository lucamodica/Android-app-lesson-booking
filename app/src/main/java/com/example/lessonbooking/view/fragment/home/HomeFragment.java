package com.example.lessonbooking.view.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.lessonbooking.view.activity.LoginActivity;
import com.example.lessonbooking.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Context ctx;
    Resources.Theme theme;
    LessonsRecyclerViewAdapter adapter;
    String account, role;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Context and ViewModel setup
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container,
                false);
        View root = binding.getRoot();
        ctx = root.getContext();
        theme = ctx.getTheme();

        //Get params from Intent and setting it
        role = ((MainActivity) requireActivity()).getRole();
        ((TextView) root.findViewById(R.id.role_text)).setText("Ruolo: " + role);
        if (role.equals("utente") || role.equals("amministratore")){
            account = ((MainActivity) requireActivity()).getAccount();
            ((TextView) root.findViewById(R.id.account_text)).
                    setText("Nome utente: " + account);
        }

        root.findViewById(R.id.logout_btn).setOnClickListener(v -> logout());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable
            Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView and adapter setup
        RecyclerView recyclerView = requireView().findViewById(R.id.RecyclerLessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        adapter = new LessonsRecyclerViewAdapter(ctx, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        //ViewModel binding setup
        homeViewModel.getLessons().observe(getViewLifecycleOwner(),
                System.out::println
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void logout(){
        String url = getString(R.string.servlet_url) +
                "logout";

        RequestManager.getInstance(ctx).makeRequest(Request.Method.GET,
                url, this::handleLogoutResponse
        );
    }

    public void handleLogoutResponse(JSONObject jsonResult){
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
                    requireActivity().startActivity(new Intent(ctx,
                            LoginActivity.class));
                    requireActivity().finish();
                    break;

                case "no_user":
                    Toast.makeText(ctx, "Nessun utente loggato per " +
                                    "effettuare logout", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        catch (JSONException ed) {
            ed.printStackTrace();
        }
    }


    public void fetchLessons(){
        String url = getString(R.string.servlet_url) +
                "selectElems?objType=ripetizione&user=" ;
    }
}