package com.example.lessonbooking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lessonbooking.R;
import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.utilities.PostDiffCallback;

import java.util.List;


public class LessonsRecyclerViewAdapter extends
        RecyclerView.Adapter<LessonsRecyclerViewAdapter.ViewHolder> {

     //Stores and recycles views as they are scrolled off screen
     public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
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
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { }
    }


    private List<Lesson> lData;
    private final LayoutInflater sInflater;
    private final Resources res;
    Resources.Theme theme;


    //Data is passed into the constructor
    public LessonsRecyclerViewAdapter(Context context, List<Lesson> data) {
        this.sInflater = LayoutInflater.from(context);
        this.res = context.getResources();
        this.theme = context.getTheme();
        this.lData = data;
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

        setStatusColor(holder.status);
    }

    //Set the textColor to the status
    @SuppressLint("ResourceAsColor")
    void setStatusColor(TextView statusText){
        int color;
        switch (statusText.getText().toString()){
            case "attiva":
                color = res.getColor(R.color.primary, theme);
                break;

            case "effettuata":
                color = res.getColor(R.color.success, theme);
                break;

            case "disdetta":
                color = res.getColor(R.color.danger, theme);
                break;

            default:
                color = res.getColor(R.color.black, theme);
        }

        statusText.setTextColor(color);
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