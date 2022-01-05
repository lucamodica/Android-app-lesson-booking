package com.example.lessonbooking.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.lessonbooking.view.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    private List<Slot> sData;
    private final Context ctx;
    private final LayoutInflater sInflater;
    private final String accountForBooking;
    private long lastClickTime = 0;

    //Data is passed into the constructor
    public SlotsRecyclerViewAdapter(Context context, List<Slot> data,
                                    String accountForBooking) {
        this.sInflater = LayoutInflater.from(context);
        this.ctx = context;
        this.sData = data;
        this.accountForBooking = accountForBooking;
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
            holder.book.setOnClickListener(v -> bookSlot(
                    slot.getTime_slot(), slot.getDay(),
                    slot.getId_number(), slot.getCourse()
            ));
        }
        holder.book.setVisibility(visibility);
    }

    private void bookSlot(String t_slot, String day,
                          String teacher, String course){

        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        //Reqeust url
        String url = ctx.getString(R.string.servlet_url) +
                "insert?objType=ripetizione&" +
                "teacher=" + teacher + "&t_slot=" + t_slot +
                "&day=" + day + "&status=attiva&user=" + accountForBooking +
                "&course=" + course;

        //Make request
        RequestManager.getInstance(ctx).makeRequest(
                Request.Method.POST, url,
                this::handleBookSlotResponse
        );
    }
    //TODO to be reviewed the switch case
    private void handleBookSlotResponse(JSONObject obj){

        try{
            String result = obj.getString("result");
            switch (result) {
                case "success":
                    Toast.makeText(ctx, "Lezione prenotata!",
                            Toast.LENGTH_SHORT).show();
                    break;

                case "no_user":
                    Toast.makeText(ctx, R.string.no_user_result,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ctx, LoginActivity.class);
                    ctx.startActivity(i);
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
    }
}