package com.example.lessonbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lessonbooking.model.Slot;

import java.util.List;

/*
public class SlotsRecyclerViewAdapter extends RecyclerView.Adapter<SlotsRecyclerViewAdapter.ViewHolder> {

    
    private List<Slot> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    SlotsRecyclerViewAdapter(Context context, List<Slot> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserContact user = mData.get(position);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());
        holder.email.setText(user.getEmail());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView email;
        TextView phone;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(),"CLICK: "+getBindingAdapterPosition(),Toast.LENGTH_LONG).show();
        }
    }

    public void setData(List<UserContact> newData) {

        // Versione vecchia:  notifyDataSetChanged(); aggiorna TUTTA la lista
        if (mData != null) {
            PostDiffCallback postDiffCallback = new PostDiffCallback(mData, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            mData.clear();
            mData.addAll(newData);
            diffResult.dispatchUpdatesTo(this);// richiama onBindviewHolder
        } else {
            // first initialization
            mData = newData;
        }
    }

    class PostDiffCallback extends DiffUtil.Callback {

        private final List<UserContact> oldPosts, newPosts;

        public PostDiffCallback(List<UserContact> oldPosts, List<UserContact> newPosts) {
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
            return oldPosts.get(oldItemPosition).getName() == newPosts.get(newItemPosition).getName();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }
    
    
} */