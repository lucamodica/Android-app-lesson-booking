package com.example.lessonbooking.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lessonbooking.R;
import com.example.lessonbooking.databinding.FragmentCatalogBinding;
import com.example.lessonbooking.utilities.SlotsListsManager;

public class CatalogFragment extends Fragment{

    //Main vars
    private FragmentCatalogBinding binding;
    View root;
    SlotsListsManager slotsListsManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slotsListsManager = new SlotsListsManager(this,
                R.id.recycler_slots_catalog);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        slotsListsManager = null;
    }
}