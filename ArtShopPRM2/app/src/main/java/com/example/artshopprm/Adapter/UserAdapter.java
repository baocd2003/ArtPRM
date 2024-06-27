package com.example.artshopprm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.ChatActivity;
import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private List<Account> userLists;
    private String currentUserID;

    public UserAdapter(Context context, String currentUserID) {
        this.context = context;
        this.currentUserID = currentUserID;
        this.userLists = new ArrayList<>();
    }

    public void add(Account acc) {
        userLists.add(acc);
        notifyDataSetChanged();
    }

    public void clear() {
        userLists.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Account user = userLists.get(position);
        if (user != null && !user.getId().equals(currentUserID)) {
            holder.name.setText(user.getUserName());
            holder.email.setText(user.getEmail());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("id", user.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView email, name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.email);
        }
    }
}
