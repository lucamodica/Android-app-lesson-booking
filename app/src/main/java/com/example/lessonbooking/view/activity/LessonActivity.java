package com.example.lessonbooking.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonbooking.R;
import com.example.lessonbooking.model.Lesson;


public class LessonActivity extends AppCompatActivity {

    Lesson lesson;

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

        setContentView(R.layout.activity_lesson);

        ((TextView) findViewById(R.id.textView3)).setText(lesson.toString());
    }
}