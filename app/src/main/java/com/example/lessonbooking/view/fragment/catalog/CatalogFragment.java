package com.example.lessonbooking.view.fragment.catalog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.adapter.SlotsRecyclerViewAdapter;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.FragmentCatalogBinding;
import com.example.lessonbooking.model.Slot;
import com.example.lessonbooking.utilities.GenericUtils;
import com.example.lessonbooking.view.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CatalogFragment extends Fragment implements View.OnClickListener{

    //Main vars
    private CatalogViewModel catalogViewModel;
    private FragmentCatalogBinding binding;
    Context ctx;
    HashMap<String, List<Slot>> catalog;
    Resources.Theme theme;
    SlotsRecyclerViewAdapter adapter;
    //Vars for the button group
    private final int[] btns_id = {R.id.Lunedi, R.id.Martedi,
            R.id.Mercoledi, R.id.Giovedi, R.id.Venerdi};
    private Button btn_unfocus;

    //TODO the waiting TextView screen to show before the catalog fetch
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //Context and ViewModel setup
        catalogViewModel =
                new ViewModelProvider(this).get(CatalogViewModel.class);
        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ctx = root.getContext();
        theme = ctx.getTheme();

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Fetching the available slots catalog
        fetchCatalog();

        //RecyclerView and adapter setup
        RecyclerView recyclerView = requireView().findViewById(R.id.RecyclerSlotsCatalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        adapter = new SlotsRecyclerViewAdapter(ctx, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        //ViewModel binding setup
        catalogViewModel.getSlotsCatalog().observe(getViewLifecycleOwner(),
                slotsChanged -> adapter.setData(slotsChanged)
        );
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        catalog = null;
    }

    @Override
    public void onClick(View v) {
        setFocus(btn_unfocus, requireView().findViewById(v.getId()));
        catalogViewModel.setSlotsCatalog(catalog.get(v.getResources().
                getResourceEntryName(v.getId())));
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(getResources().getColor(R.color.purple_500, theme));
        btn_unfocus.setBackgroundColor(getResources().getColor(R.color.white, theme));
        btn_focus.setTextColor(getResources().getColor(R.color.white, theme));
        btn_focus.setBackgroundColor(getResources().getColor(R.color.purple_500, theme));
        this.btn_unfocus = btn_focus;
    }
    private void setupButtonsGroup(){
        Button[] btns = new Button[5];
        for(int i = 0; i < btns.length; i++){
            btns[i] = requireView().findViewById(btns_id[i]);
            btns[i].setOnClickListener(this);
        }
        btn_unfocus = btns[0];
        btns[0].setTextColor(getResources().getColor(R.color.white, theme));
        btns[0].setBackgroundColor(getResources().getColor(R.color.purple_500, theme));
    }

    private void fetchCatalog(){
        String url = getString(R.string.servlet_url) +
                "availableSlots?objType=slots";

        RequestManager.getInstance(ctx).cancelAllRequests();
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                this::handleCatalogResponse
        );
    }
    private void createCatalog(JSONObject obj) throws JSONException {
        JSONObject arrSlots = obj.getJSONObject("slots");
        for (String day: GenericUtils.getLessonDays()) {
            JSONArray daySlots = arrSlots.getJSONArray(day);
            for (int i = 0; i < daySlots.length(); i++) {
                JSONObject slot = daySlots.getJSONObject(i);
                Objects.requireNonNull(catalog.get(day)).add(new Slot(
                        slot.getString("time_slot"),
                        slot.getString("id_number"),
                        slot.getString("teacher_name"),
                        slot.getString("teacher_surname"),
                        slot.getString("course")
                ));
            }
        }

        catalogViewModel.setSlotsCatalog(catalog.get("Lunedi"));
    }
    private void setupCatalogView(JSONObject obj) throws JSONException{

        //Setting up the hashmap for the fetched catalog
        catalog = new HashMap<>();
        for (String day: GenericUtils.getLessonDays()) {
            catalog.put(day, new ArrayList<>());
        }

        //Button group setup
        setupButtonsGroup();

        //Fill the HashMap with the fetched slots
        createCatalog(obj);
    }
    private void handleCatalogResponse(JSONObject obj){
        try {
            String result = obj.getString("result");
            switch (result) {
                case "success":
                    setupCatalogView(obj);
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