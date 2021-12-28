package com.example.lessonbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lessonbooking.R;
import com.example.lessonbooking.model.Slot;
import com.example.lessonbooking.model.Teacher;

import java.util.List;


public class SlotsRecyclerViewAdapter extends
        RecyclerView.Adapter<SlotsRecyclerViewAdapter.ViewHolder> {

    //Stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView t_slot;
        TextView teacher;
        TextView course;

        ViewHolder(View itemView) {
            super(itemView);
            t_slot = itemView.findViewById(R.id.t_slot_field);
            teacher = itemView.findViewById(R.id.teacher_field);
            course = itemView.findViewById(R.id.course_field);
            //itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) { }
    }

    static class PostDiffCallback extends DiffUtil.Callback {

        private final List<Slot> oldPosts, newPosts;

        public PostDiffCallback(List<Slot> oldPosts, List<Slot> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }


    private List<Slot> sData;
    private final LayoutInflater sInflater;

    //Data is passed into the constructor
    public SlotsRecyclerViewAdapter(Context context, List<Slot> data) {
        this.sInflater = LayoutInflater.from(context);
        this.sData = data;
    }

    //Inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = sInflater.inflate(R.layout.slot_row, parent, false);
        return new ViewHolder(view);
    }

    //Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Slot slot = sData.get(position);
        Teacher teacher = new Teacher(slot.getId_number(),
                slot.getTeacher_name(), slot.getTeacher_surname());

        holder.t_slot.setText(slot.getTime_slot());
        holder.course.setText(teacher.toString());
        holder.teacher.setText(slot.getCourse());
    }

    //Total number of rows
    @Override
    public int getItemCount() {
        return sData.size();
    }

    public void setData(List<Slot> newData) {

        if (sData != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(sData, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            sData.clear();
            sData.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
        else {
            sData = newData;
        }
    }
    
    
}