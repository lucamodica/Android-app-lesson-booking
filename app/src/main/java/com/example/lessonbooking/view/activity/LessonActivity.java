package com.example.lessonbooking.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonbooking.R;
import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.model.Teacher;
import com.example.lessonbooking.utilities.GenericUtils;


public class LessonActivity extends AppCompatActivity {

    public static class StatusViewModel extends ViewModel {

        private final MutableLiveData<String> status;

        public StatusViewModel() {
            status = new MutableLiveData<>();
        }

        public LiveData<String> getStatus() {
            return status;
        }
    }


    private Lesson lesson;
    private StatusViewModel statusViewModel;
    private Context ctx;
    private TextView statusField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get params from Intent
        Bundle b = getIntent().getExtras();
        if (b.containsKey("lesson")){
            lesson = (Lesson) getIntent().getSerializableExtra("lesson");
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

        //Set the bind for the status field. This to dynamically
        //update the new status and its own color if the user
        //decide to update the lesson status
        statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        statusViewModel.getStatus().observe(this,
                this::updateStatusInLayout
        );

        //Setting the lesson content in the layout
        setContentLesson();
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
        GenericUtils.setStatusColor(ctx, statusField);
    }

    private void updateLessonStatus(String status){

    }
    private void handleupdateLessonStatusResponse(){

    }
}