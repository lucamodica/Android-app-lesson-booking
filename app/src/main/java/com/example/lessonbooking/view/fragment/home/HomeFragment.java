package com.example.lessonbooking.view.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lessonbooking.R;
import com.example.lessonbooking.adapter.LessonsRecyclerViewAdapter;
import com.example.lessonbooking.databinding.FragmentHomeBinding;
import com.example.lessonbooking.view.activity.MainActivity;

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
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ctx = root.getContext();
        theme = ctx.getTheme();

        /*
        Intent i = requireActivity().getIntent();
        if (i.getExtras().containsKey("account") &&
                i.getExtras().containsKey("role")){
            account = i.getStringExtra("account");
            role = i.getStringExtra("role");
        }
        else {
            Toast.makeText(ctx, "Parametri mancanti nell'Intent!",
                    Toast.LENGTH_LONG).show();
            requireActivity().finish();
        }

         */

        //Get params from Intent and setting it
        role = ((MainActivity) requireActivity()).getRole();
        TextView text_role =  root.findViewById(R.id.role_text);
        text_role.setText("Ruolo: " + role);
        System.out.println(role);
        if (role.equals("utente") || role.equals("amministratore")){
            account = ((MainActivity) requireActivity()).getAccount();
            TextView text_account = root.findViewById(R.id.account_text);
            text_account.setText("Nome utente: " + account);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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


    public void fetchCatalog(){
        String url = getString(R.string.servlet_url) +
                "selectElems?objType=ripetizione&user=" ;

        /*
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                this::setupCatalog
        );

         */
    }
}