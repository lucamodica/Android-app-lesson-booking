package com.example.lessonbooking.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.LogoutManager;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.databinding.SelectCourseTeacherBinding;
import com.example.lessonbooking.model.Course;
import com.example.lessonbooking.model.Model;
import com.example.lessonbooking.model.Teacher;
import com.example.lessonbooking.utilities.SlotsListsManager;
import com.example.lessonbooking.connectivity.SuccessHandler;
import com.example.lessonbooking.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;


public class BookingFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private SelectCourseTeacherBinding binding;
    private View root;
    private Context ctx;
    private long lastClickTime = 0;
    SlotsListsManager slotsListsManager;

    Button nextBtn;
    Button backBtn;
    TextView teacherTitle;
    TextView waitingCourseText;
    TextView waitingTeacherText;
    Spinner coursesSpinner;
    Spinner teachersSpinner;

    private String account, role;
    private ArrayList<String> listIds;
    private String selectedCourse, selectedTeacher;
    private boolean selecting; //false = course, true = teacher

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
        //will be showed; otherwise the necessary component
        //will be initialized
        if (role.equals("ospite")){
            showLoginSuggest();
        }
        else {
            initCompnent();
        }

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (role.equals("utente") || role.equals("amministratore")){
            selecting = false;
            root.findViewById(R.id.spinner_courses).
                    setVisibility(View.GONE);
            fetchData("corso");
        }
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
                setOnClickListener(v -> LogoutManager.
                        getInstance(ctx, account, role).makeLogout());
    }
    private void initCompnent(){

        //Next/back buttons
        nextBtn = root.findViewById(R.id.button_next);
        backBtn = root.findViewById(R.id.button_back);

        //Spinners
        coursesSpinner = root.findViewById(R.id.spinner_courses);
        teachersSpinner = root.findViewById(R.id.spinner_teachers);

        //TextViews
        teacherTitle = root.findViewById(R.id.select_teacher_title);
        waitingCourseText = root.findViewById(R.id.waiting_course_text);
        waitingTeacherText = root.findViewById(R.id.waiting_teacher_text);
    }
    private void onBackPressed(View v){
        if (selecting){
            teacherTitle.setVisibility(View.GONE);
            teachersSpinner.setVisibility(View.GONE);
            waitingTeacherText.setVisibility(View.GONE);
            waitingTeacherText.setText(getString(R.string.waiting));

            selecting = false;
            selectedCourse = null;
            waitingCourseText.setVisibility(View.VISIBLE);
            coursesSpinner.setVisibility(View.GONE);
            backBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            coursesSpinner.setEnabled(true);
            fetchData("corso");
        }
    }
    private void onNextPressed(View v){
        if (selecting){
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            setViewLayout(R.layout.fragment_booking);
            slotsListsManager = new SlotsListsManager(this,
                    R.id.recycler_bookable_slots, account, selectedCourse,
                    selectedTeacher);
        }
        else {
            selecting = true;
            waitingTeacherText.setVisibility(View.VISIBLE);
            teacherTitle.setVisibility(View.VISIBLE);
            coursesSpinner.setEnabled(false);

            fetchData("affiliazione");
        }
    }
    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) requireActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        Objects.requireNonNull(rootView).removeAllViews();
        rootView.addView(root);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (selecting){
            selectedTeacher = listIds.get(i);
        }
        else {
            selectedCourse = listIds.get(i);
        }
        System.out.println("Item selected: " + listIds.get(i));
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void fetchData(String objType){

        //Request url
        String action = objType.equals("corso") ?
                "selectTable?objType=" + objType :
                "selectElems?objType=" + objType + "&course_title=" + selectedCourse;
        String url = getString(R.string.servlet_url) + action;

        //Prevent button event
        nextBtn.setOnClickListener(null);
        backBtn.setOnClickListener(null);

        //Make request
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.GET, url,
                (SuccessHandler)  obj -> {
                    JSONArray arr = obj.getJSONArray("content");
                    createDropdown(arr, objType);
                    setContentDropdowns(objType, arr.length());
                }
        );
    }
    private void createDropdown(JSONArray arr, String objType)
            throws JSONException {

        ArrayList<String> list = new ArrayList<>();
        Model elem;

        listIds = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            elem = (objType.equals("corso")) ? new Course(arr.getJSONObject(i))
                    : new Teacher(arr.getJSONObject(i));
            list.add(elem.toString());
            listIds.add(elem.getModelId());
        }
        System.out.println(list.toString());
        System.out.println(listIds.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,
                android.R.layout.simple_spinner_dropdown_item, list);
        Spinner spinner = objType.equals("corso") ? coursesSpinner
                        : teachersSpinner;
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
    }
    @SuppressLint("SetTextI18n")
    private void setContentDropdowns(String objType, int result_length){

        //Show spinner
        Spinner spinner = objType.equals("corso") ? coursesSpinner
                : teachersSpinner;
        spinner.setVisibility(View.VISIBLE);

        //Buttons handle
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(this::onNextPressed);
        backBtn.setVisibility((objType.equals("corso")) ? View.GONE
                : View.VISIBLE);
        backBtn.setOnClickListener(this::onBackPressed);

        //Waiting text handle
        TextView waitingText = (objType.equals("corso")) ? waitingCourseText
                : waitingTeacherText;
        waitingText.setVisibility(View.GONE);

        //Case in which none of the teacher teach the couse
        //previously selected
        if (result_length == 0){
            nextBtn.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            waitingText.setVisibility(View.VISIBLE);
            waitingText.setText(
                    (objType.equals("corso")) ?
                    "Nessun corso dispobile" :
                    "Nessun docente disponibile per la materia: " + selectedCourse

            );
        }
    }
}