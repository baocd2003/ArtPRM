package com.example.artshopprm.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Entity.OrderDetail;
import com.example.artshopprm.OrderDetailActivity;
import com.example.artshopprm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderDetail> orderDetailList;

    public OrderDetailAdapter(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        holder.orderIdTextView.setText("Order ID: " + orderDetail.getOrderId());
        holder.quantityTextView.setText("Quantity: " + orderDetail.getQuantity());
        holder.actualPriceTextView.setText("Actual Price: $" + orderDetail.getActualPrice());

        // Retrieve art details
        DatabaseReference artRef = FirebaseDatabase.getInstance().getReference("arts").child(orderDetail.getArtId());
        artRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String artName = dataSnapshot.child("artName").getValue(String.class);
                    String artDescription = dataSnapshot.child("description").getValue(String.class);
                    Integer rate = dataSnapshot.child("rate").getValue(Integer.class);
                    String author = dataSnapshot.child("author").getValue(String.class);

                    // Set art details to TextViews
                    holder.artNameTextView.setText("Art Name: " + artName);
                    holder.artDescriptionTextView.setText("Description: " + artDescription);
                    holder.rateTextView.setText("Rate: " + rate);
                    holder.authorTextView.setText("Author: " + author);

                } else {
                    Toast.makeText(holder.itemView.getContext(), "Art not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("OrderDetailAdapter", "Failed to retrieve art details: " + databaseError.getMessage());
                Toast.makeText(holder.itemView.getContext(), "Failed to retrieve art details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, artNameTextView, artDescriptionTextView, rateTextView, authorTextView, quantityTextView, actualPriceTextView;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            artNameTextView = itemView.findViewById(R.id.artNameTextView);
            artDescriptionTextView = itemView.findViewById(R.id.artDescriptionTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            actualPriceTextView = itemView.findViewById(R.id.actualPriceTextView);
        }
    }
}