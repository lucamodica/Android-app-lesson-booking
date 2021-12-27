package com.example.lessonbooking.view.fragment.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lessonbooking.model.Slot;

import java.util.HashMap;
import java.util.List;

public class CatalogViewModel extends ViewModel {

    private final MutableLiveData<HashMap<String, List<Slot>>> slotsCatalog;

    public CatalogViewModel() {
        slotsCatalog = new MutableLiveData<>();
    }

    public LiveData<HashMap<String, List<Slot>>>
        getSlotsCatalog (){
        return slotsCatalog;
    }

    public void setSlotsCatalog(HashMap<String, List<Slot>> slots){
        slotsCatalog.setValue(slots);
    }
}