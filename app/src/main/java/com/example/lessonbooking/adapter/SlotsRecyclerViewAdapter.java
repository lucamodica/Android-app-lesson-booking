package com.example.lessonbooking.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.RequestManager;
import com.example.lessonbooking.model.Slot;
import com.example.lessonbooking.model.Teacher;
import com.example.lessonbooking.utilities.PostDiffCallback;
import com.example.lessonbooking.connectivity.SuccessHandler;

import java.util.List;


public class SlotsRecyclerViewAdapter extends
        RecyclerView.Adapter<SlotsRecyclerViewAdapter.ViewHolder> {

    //Stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView t_slot;
        TextView teacher;
        TextView course;
        Button book;

        ViewHolder(View itemView) {
            super(itemView);
            t_slot = itemView.findViewById(R.id.t_slot_field);
            teacher = itemView.findViewById(R.id.teacher_field);
            course = itemView.findViewById(R.id.course_field);
            book = itemView.findViewById(R.id.book_btn);
        }
    }


    private List<Slot> sData, originalList;
    private final TextView waiting;
    private final Context ctx;
    private final LayoutInflater sInflater;
    private final String accountForBooking;
    private long lastClickTime = 0;

    //Data is passed into the constructor
    public SlotsRecyclerViewAdapter(Context context, List<Slot> data,
                                    String accountForBooking, TextView waiting) {
        this.sInflater = LayoutInflater.from(context);
        this.ctx = context;
        this.sData = data;
        this.accountForBooking = accountForBooking;
        this.waiting = waiting;
    }

    public List<Slot> getAdapterList() {
        return originalList;
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
        int visibility = View.GONE;

        holder.t_slot.setText(slot.getTime_slot());
        holder.course.setText(slot.getCourse());
        holder.teacher.setText(teacher.toString());

        if (accountForBooking != null){
            visibility = View.VISIBLE;
            holder.book.setOnClickListener(v -> bookSlot(position));
        }
        holder.book.setVisibility(visibility);
    }

    private void bookSlot(int slotPosition){

        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        //Reqeust url
        System.out.println(sData);
        Slot s = sData.get(slotPosition);
        String url = ctx.getString(R.string.servlet_url) +
                "insert?objType=ripetizione&" +
                "teacher=" + s.getId_number() + "&t_slot=" + s.getTime_slot() +
                "&day=" + s.getDay() + "&status=attiva&user=" + accountForBooking +
                "&course=" + s.getCourse();

        //Make request
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.POST, url,
                (SuccessHandler) obj -> {
                    Toast.makeText(ctx, "Lezione prenotata!",
                            Toast.LENGTH_SHORT).show();
                    sData.remove(slotPosition);
                    originalList.remove(slotPosition);
                    notifyItemRemoved(slotPosition);

                    if (sData.isEmpty()){
                        waiting.setText(ctx.getString(R.string.empty_slots_list));
                    }
                }
        );
    }

    //Total number of rows
    @Override
    public int getItemCount() {
        return sData.size();
    }

    public void setData(List<Slot> newData) {

        if (sData != null) {
            PostDiffCallback<Slot> postDiffCallback =
                    new PostDiffCallback<>(sData, newData);
            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(postDiffCallback);
            sData.clear();
            sData.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
        else {
            sData = newData;
        }
        originalList = newData;
    }
}