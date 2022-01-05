package com.example.lessonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lessonbooking.R;
import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.utilities.GenericUtils;
import com.example.lessonbooking.utilities.PostDiffCallback;
import com.example.lessonbooking.view.activity.LessonActivity;

import java.util.List;


public class LessonsRecyclerViewAdapter extends
        RecyclerView.Adapter<LessonsRecyclerViewAdapter.ViewHolder> {

     //Stores and recycles views as they are scrolled off screen
     public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView t_slot;
        TextView day;
        TextView status;
        Button info_btn;

        ViewHolder(View itemView) {
            super(itemView);
            t_slot = itemView.findViewById(R.id.t_slot_lesson_field);
            day = itemView.findViewById(R.id.day_lesson_field);
            status = itemView.findViewById(R.id.status_lesson_field);
            info_btn = itemView.findViewById(R.id.info_lesson_btn);
        }
    }


    private List<Lesson> lData;
    private final LayoutInflater sInflater;
    Context ctx;
    ActivityResultLauncher<Intent> lessonInfoLauncher;

    //Data is passed into the constructor
    public LessonsRecyclerViewAdapter(Context ctx, List<Lesson> data,
                                      ActivityResultLauncher<Intent> lessonInfoLauncher) {
        this.sInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.lData = data;
        this.lessonInfoLauncher = lessonInfoLauncher;
    }

    //Inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = sInflater.inflate(R.layout.lesson_row, parent, false);
        return new ViewHolder(view);
    }

    //Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lesson lesson = lData.get(position);

        holder.t_slot.setText(lesson.getT_slot());
        holder.day.setText(lesson.getDay());
        holder.status.setText(lesson.getStatus());

        GenericUtils.setStatusColor(ctx, holder.status);
        holder.info_btn.setOnClickListener(
                v -> openInfoLesson(v, lesson, position));
    }

    private void openInfoLesson(View v, Lesson lesson, int position) {

        //Intent to take the user to the LessonActivity
        Intent inte = new Intent(v.getContext(), LessonActivity.class);
        inte.putExtra("lesson", lesson);
        inte.putExtra("lessonIndex", position);
        lessonInfoLauncher.launch(inte);
    }

    //Total number of rows
    @Override
    public int getItemCount() {
        return lData.size();
    }

    public void setData(List<Lesson> newData) {

        if (lData != null) {
            PostDiffCallback<Lesson> postDiffCallback = new PostDiffCallback<>(lData, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            lData.clear();
            lData.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
        else {
            lData = newData;
        }
    }
}