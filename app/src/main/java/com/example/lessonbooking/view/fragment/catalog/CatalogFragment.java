package com.example.lessonbooking.view.fragment.catalog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.FragmentCatalogBinding;
import com.example.lessonbooking.model.Slot;
import com.example.lessonbooking.utilities.GenericUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CatalogFragment extends Fragment {

    private CatalogViewModel catalogViewModel;
    private FragmentCatalogBinding binding;
    Context ctx;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        catalogViewModel =
                new ViewModelProvider(this).get(CatalogViewModel.class);

        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ctx = root.getContext();

        final TextView textSlotsCatalog = binding.textSlotsCatalog;
        catalogViewModel.getSlotsCatalog().observe(getViewLifecycleOwner(),
                slotsChanged -> {
                    // Update the UI, in this case, a TextView.
                    textSlotsCatalog.setText(Objects.requireNonNull(slotsChanged).
                            toString());
                });

        fetchCatalog();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fetchCatalog(){
        String url = getString(R.string.servlet_url) +
                "availableSlots?objType=slots";

        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                this::setupCatalog
        );
    }

    public void setupCatalog(JSONObject obj){
        //Setting up the hashmap for the fetched catalog
        HashMap<String, List<Slot>> catalog = new HashMap<>();
        for (String day: GenericUtils.getLessonDays()) {
            catalog.put(day, new ArrayList<>());
        }

        try {
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

            catalogViewModel.setSlotsCatalog(catalog);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}