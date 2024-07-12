package com.example.artshopprm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Entity.Order;
import com.example.artshopprm.OrderDetailActivity;
import com.example.artshopprm.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;

    public OrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.sequenceNumberTextView.setText("Order #" + (position + 1));
        holder.orderStatusTextView.setText("Status: " + order.getStatus());
        holder.deliveryAddressTextView.setText("Delivery Address: " + order.getDeliveryAddress());
        holder.createdDateTextView.setText("Created Date: " + order.getCreatedDate());
        holder.updateDateTextView.setText("Update Date: " + order.getUpdateDate());
        holder.accountIdTextView.setText("Total Price: " + order.getTotalPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView sequenceNumberTextView;
        TextView orderStatusTextView;
        TextView deliveryAddressTextView;
        TextView createdDateTextView;
        TextView updateDateTextView;
        TextView accountIdTextView;

        OrderViewHolder(View itemView) {
            super(itemView);
            sequenceNumberTextView = itemView.findViewById(R.id.sequenceNumberTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            deliveryAddressTextView = itemView.findViewById(R.id.deliveryAddressTextView);
            createdDateTextView = itemView.findViewById(R.id.createdDateTextView);
            updateDateTextView = itemView.findViewById(R.id.updateDateTextView);
            accountIdTextView = itemView.findViewById(R.id.accountIdTextView);
        }
    }
}