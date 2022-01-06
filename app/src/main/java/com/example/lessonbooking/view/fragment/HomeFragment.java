package com.example.lessonbooking.view.fragment;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.adapter.LessonsRecyclerViewAdapter;
import com.example.lessonbooking.connectivity.LogoutManager;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.FragmentHomeBinding;
import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.utilities.SuccessHandler;
import com.example.lessonbooking.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public static class HomeViewModel extends ViewModel {

        private final MutableLiveData<List<Lesson>> lessons;

        public HomeViewModel() {
            lessons = new MutableLiveData<>();
        }

        public LiveData<List<Lesson>> getLessons() {
            return lessons;
        }

        public void setLessons(List<Lesson> newLessons){
            lessons.setValue(newLessons);
        }
    }


    //ViewModel and main fragment vars
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Context ctx;
    View root;
    TextView waitingText;
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
        waitingText = root.findViewById(R.id.waiting_home);

        //Get params from Intent and setting it
        role = ((MainActivity) requireActivity()).getRole();
        ((TextView) root.findViewById(R.id.role_text)).setText("Ruolo: " + role);
        if (role.equals("utente") || role.equals("amministratore")){
            account = ((MainActivity) requireActivity()).getAccount();
            ((TextView) root.findViewById(R.id.account_text)).
                    setText("Nome utente: " + account);
        }

        //Setting the logout button
        root.findViewById(R.id.logout_btn).setOnClickListener(v -> LogoutManager.
                getInstance(ctx, account, role).makeLogout());

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

        waitingText.setText("");

        root.findViewById(R.id.suggest_login_home_layout).
                setVisibility(View.VISIBLE);

        root.findViewById(R.id.suggest_login_home_btn).
                setOnClickListener(v -> LogoutManager.
                        getInstance(ctx, account, role).makeLogout());
    }

    private void fetchLessons(){
        String url = getString(R.string.servlet_url) + "selectElems?" +
                "objType=ripetizione&user=" + account;

        RequestManager.getInstance(ctx).cancelAllRequests();
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                (SuccessHandler) this::setupCatalogView
        );
    }
    private void setupCatalogView(JSONObject obj){

        //Setting up the hashmap for the fetched catalog
        lessons = new ArrayList<>();
        try {

            //Remove the waiting warning
            waitingText.setText("");

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
            if (lessons.isEmpty()){
                waitingText.setText(ctx.getString(R.string.empty_lessons_list));
            }
        }
        catch (IllegalStateException | JSONException e){
            System.out.println(e.getMessage());
        }

    }
}