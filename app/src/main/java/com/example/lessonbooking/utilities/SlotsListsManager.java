package com.example.lessonbooking.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.adapter.SlotsRecyclerViewAdapter;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.model.Slot;
import com.example.lessonbooking.view.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SlotsListsManager implements View.OnClickListener{

    public static class SlotsViewModel extends ViewModel {

        private final MutableLiveData<List<Slot>> slotsList;

        public SlotsViewModel() {
            slotsList = new MutableLiveData<>();
        }

        public LiveData<List<Slot>> getSlotsList(){
            return slotsList;
        }

        public void setSlotsList(List<Slot> newSlotsList){
            slotsList.setValue(newSlotsList);
        }
    }


    private final int[] btns_id = {R.id.Lunedi, R.id.Martedi,
            R.id.Mercoledi, R.id.Giovedi, R.id.Venerdi};
    private Button btn_unfocus;
    private TextView waitingText;

    private Context ctx;
    private Resources res;
    private Resources.Theme theme;
    private View view;
    private SlotsViewModel model;
    private HashMap<String, List<Slot>> lists;
    private SlotsRecyclerViewAdapter adapter;
    private String currentDay, account = null, selectedCourse, selectedTeacher;

    public SlotsListsManager(Fragment fragment, int recyclerViewId) {
        init(fragment, recyclerViewId);
    }
    public SlotsListsManager(Fragment fragment, int recyclerViewId, String account,
                             String selectedCourse, String selectedTeacher) {

        this.selectedCourse = selectedCourse;
        this.selectedTeacher = selectedTeacher;
        this.account = account;
        init(fragment, recyclerViewId);
    }
    private void init(Fragment fragment, int recyclerViewId){

        this.ctx = fragment.getContext();
        this.view = fragment.getView();

        RecyclerView recyclerView = Objects.requireNonNull(view).findViewById(recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        res = ctx.getResources();
        theme = ctx.getTheme();
        waitingText = view.findViewById(R.id.waiting);

        //Adapter setup
        adapter = new SlotsRecyclerViewAdapter(ctx, new ArrayList<>(), account, waitingText);
        recyclerView.setAdapter(adapter);

        //ViewModel binding setup
        model = new ViewModelProvider(fragment).
                get(SlotsViewModel.class);
        model.getSlotsList().observe(fragment.getViewLifecycleOwner(),
                slotsChanged -> adapter.setData(slotsChanged)
        );

        //Start generating the slots lists
        fetchSlots((account == null) ? "slots" : "lessonSlots");
    }

    @Override
    public void onClick(View v) {
        waitingText.setText("");
        setFocus(btn_unfocus, view.findViewById(v.getId()));
        lists.replace(currentDay, adapter.getAdapterList());
        System.out.println(currentDay + ": " + lists.get(currentDay));

        String newDay = v.getResources().getResourceEntryName(v.getId());
        System.out.println("New day: " + newDay);
        List<Slot> newList = lists.get(newDay);
        System.out.println("New list: " + newList);
        currentDay = newDay;
        model.setSlotsList(newList);

        if (Objects.requireNonNull(lists.get(newDay)).isEmpty()){
            System.out.println("empty");
            waitingText.setText(ctx.getString(R.string.empty_slots_list));
        }
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(res.getColor(R.color.app_theme, theme));
        btn_unfocus.setBackgroundColor(res.getColor(R.color.white, theme));
        btn_focus.setTextColor(res.getColor(R.color.white, theme));
        btn_focus.setBackgroundColor(res.getColor(R.color.app_theme, theme));
        this.btn_unfocus = btn_focus;
    }
    private void setupButtonsGroup(){
        Button[] btns = new Button[5];
        for(int i = 0; i < btns.length; i++){
            btns[i] = view.findViewById(btns_id[i]);
            btns[i].setOnClickListener(this);
        }
        btn_unfocus = btns[0];
        btns[0].setTextColor(res.getColor(R.color.white, theme));
        btns[0].setBackgroundColor(res.getColor(R.color.app_theme, theme));
    }

    private void fetchSlots(String objType){
        String action = (account == null) ?
                "" : "&course=" + selectedCourse +
                        "&teacher=" + selectedTeacher;
        String url = ctx.getString(R.string.servlet_url) +
                "availableSlots?objType=" + objType + action;

        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                (SuccessHandler) this::setupSlotsView
        );
    }
    private void createSlots(JSONObject obj) throws JSONException {

        waitingText.setText("");
        JSONObject arrSlots = obj.getJSONObject("slots");
        for (String day: GenericUtils.getLessonDays()) {
            JSONArray daySlots = arrSlots.getJSONArray(day);
            for (int i = 0; i < daySlots.length(); i++) {
                JSONObject slot = daySlots.getJSONObject(i);
                Objects.requireNonNull(lists.get(day)).add(new Slot(
                        slot.getString("time_slot"),
                        slot.getString("id_number"),
                        slot.getString("teacher_name"),
                        slot.getString("teacher_surname"),
                        slot.getString("course"), day
                ));
            }
        }

        List<Slot> initList = lists.get("Lunedi");
        currentDay = "Lunedi";
        model.setSlotsList(initList);

        if (Objects.requireNonNull(initList).isEmpty()){
            waitingText.setText(ctx.getString(R.string.empty_slots_list));
        }
    }
    private void setupSlotsView(JSONObject obj) throws JSONException{

        //Setting up the hashmap for the fetched slots
        lists = new HashMap<>();
        for (String day: GenericUtils.getLessonDays()) {
            lists.put(day, new ArrayList<>());
        }

        //Button group setup
        setupButtonsGroup();

        //Fill the HashMap with the fetched slots
        createSlots(obj);
    }
}
