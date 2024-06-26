package com.example.artshopprm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.Entity.MessageModel;
import com.example.artshopprm.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context context;
    private List<MessageModel> messModelList;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messModelList = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel mess = messModelList.get(position);
        holder.msg.setText(mess.getMessages());

        // Check if senderId is null to avoid NullPointerException
        if (mess.getSenderId() != null && mess.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        }
    }


    @Override
    public int getItemCount() {
        return messModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;
        private LinearLayout main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.mainMessageLayout);
        }
    }
}
