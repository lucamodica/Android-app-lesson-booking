package com.example.lessonbooking.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.model.Teacher;
import com.example.lessonbooking.utilities.GenericUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class LessonActivity extends AppCompatActivity {

    public static class StatusViewModel extends ViewModel {

        private final MutableLiveData<String> status;

        public StatusViewModel() {
            status = new MutableLiveData<>();
        }

        public LiveData<String> getStatus() {
            return status;
        }

        public void setStatus(String newStatus){
            status.setValue(newStatus);
        }
    }


    private Lesson lesson;
    private int lessonIndex;
    private StatusViewModel statusViewModel;
    private Context ctx;
    private TextView statusField;
    private Button lessonConfirmBtn;
    private Button lessonCancelBtn;
    private Button backInfoLessonBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get params from Intent Object  and position
        Intent data = getIntent();
        if (data != null && data.hasExtra("lesson") &&
                data.hasExtra("lessonIndex")){
            lesson = (Lesson) data.getSerializableExtra("lesson");
            lessonIndex = data.getIntExtra("lessonIndex", -1);
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Parametri mancanti nell'Intent!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        //Setting the layout
        ctx = getApplicationContext();
        setContentView(R.layout.activity_lesson);
        statusField = findViewById(R.id.status_info_lesson);
        lessonConfirmBtn = findViewById(R.id.lesson_confirm_btn);
        lessonCancelBtn = findViewById(R.id.lesson_cancel_btn);
        backInfoLessonBtn = findViewById(R.id.back_info_lesson_btn);

        //Set the bind for the status field. This to dynamically
        //update the new status and its own color if the user
        //decide to update the lesson status
        statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        statusViewModel.getStatus().observe(this,
                this::updateStatusInLayout
        );

        //Setting the lesson content in the layout
        setContentLesson();

        //Setting the clickListener
        setButtonClickListener();
    }
    @Override
    public void onBackPressed() {
        closeInfoLesson();
    }
    private void setButtonClickListener(){

        int visibility;
        if (lesson.getStatus().equals("attiva")){
            visibility = View.VISIBLE;
            lessonConfirmBtn.setOnClickListener(
                    v -> updateLessonStatus("effettuata"));
            lessonCancelBtn.setOnClickListener(
                    v -> updateLessonStatus("disdetta"));
        }
        else {
            visibility = View.GONE;
        }

        findViewById(R.id.update_status_btns).setVisibility(visibility);
        backInfoLessonBtn.setOnClickListener(
                v -> closeInfoLesson());
    }
    private void setContentLesson(){

        Teacher teacher = new Teacher(
                lesson.getTeacher(),
                lesson.getTeacher_name(),
                lesson.getTeacher_surname()
        );

        ((TextView) findViewById(R.id.t_slot_info_lesson)).setText(lesson.getT_slot());
        ((TextView) findViewById(R.id.day_info_lesson)).setText(lesson.getDay());
        ((TextView) findViewById(R.id.teacher_info_lesson)).setText(teacher.toString());
        ((TextView) findViewById(R.id.course_info_lesson)).setText(lesson.getCourse());
        statusField.setText(lesson.getStatus());

        GenericUtils.setStatusColor(ctx, statusField);
    }
    private void updateStatusInLayout(String statusChanged){
        lesson.setStatus(statusChanged);
        statusField.setText(statusChanged);
        GenericUtils.setStatusColor(ctx, statusField);
    }
    private void closeInfoLesson(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("lessonStatus", lesson.getStatus());
        intent.putExtra("lessonIndex", lessonIndex);
//todo ???
        setResult(69420, intent);
        finish();
    }

    private void updateLessonStatus(String status){
        String url = getString(R.string.servlet_url) + "updateLessonStatus?" +
                "objType=ripetizione" +
                "&teacher=" + lesson.getTeacher() +
                "&t_slot=" + lesson.getT_slot() +
                "&day=" + lesson.getDay() +
                "&user=" + lesson.getUser() +
                "&newStatus=" + status;

        lessonConfirmBtn.setOnClickListener(null);
        lessonCancelBtn.setOnClickListener(null);
        backInfoLessonBtn.setOnClickListener(null);

        RequestManager.getInstance(ctx).cancelAllRequests();
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.POST, url,
                this::handleupdateLessonStatusResponse
        );
    }
    private void handleupdateLessonStatusResponse(JSONObject obj){
        try {
            String result = obj.getString("result");
            switch (result){
                case "success":
                    String newStatus = obj.getString("newStatus");
                    statusViewModel.setStatus(newStatus);
                    Toast.makeText(ctx, newStatus.equals("effettuata") ?
                            "Lezione segnata come effettuata!" :
                            "Lezione disdetta!", Toast.LENGTH_SHORT).show();

                    setButtonClickListener();
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