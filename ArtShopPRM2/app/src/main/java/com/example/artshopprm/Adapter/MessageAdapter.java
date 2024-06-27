package com.example.artshopprm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Entity.MessageModel;
import com.example.artshopprm.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context context;
    private List<MessageModel> messModelList;
    private String senderId; // Added senderId field

    public MessageAdapter(Context context, String senderId) {
        this.context = context;
        this.messModelList = new ArrayList<>();
        this.senderId = senderId;
    }

    public void add(MessageModel mess) {
        messModelList.add(mess);
        notifyDataSetChanged();
    }

    public void clear() {
        messModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            // Sender message layout (left-aligned)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_sender, parent, false);
        } else {
            // Receiver message layout (right-aligned)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_receiver, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel mess = messModelList.get(position);
        holder.msg.setText(mess.getMessages());
    }

    @Override
    public int getItemCount() {
        return messModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String currentUserId = senderId;
        String messageSenderId = messModelList.get(position).getSenderId();

        // Return view type based on message sender
        return (messageSenderId != null && messageSenderId.equals(currentUserId)) ? 0 : 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
        }
    }
}
