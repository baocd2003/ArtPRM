package com.example.artshopprm;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Adapter.OrderDetailAdapter;
import com.example.artshopprm.Entity.OrderDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private List<OrderDetail> orderDetailList;
    private RecyclerView recyclerView;
    private OrderDetailAdapter orderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        // Retrieve order ID from intent
        String orderId = getIntent().getStringExtra("orderId");
        orderDetailList = new ArrayList<>();
        if (orderId != null) {
            retrieveOrderDetailsFromFirebase(orderId);
        } else {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        orderDetailAdapter = new OrderDetailAdapter(orderDetailList);
        recyclerView.setAdapter(orderDetailAdapter);
    }

    private void retrieveOrderDetailsFromFirebase(String orderId) {
        DatabaseReference orderDetailsRef = FirebaseDatabase.getInstance().getReference("orderDetails");

        // Query to retrieve order details by orderId
        orderDetailsRef.orderByChild("orderId").equalTo(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            String id = snapshot.child("id").getValue(String.class);
                            String artId = snapshot.child("artId").getValue(String.class);
                            int quantity = snapshot.child("quantity").getValue(Integer.class);
                            double actualPrice = snapshot.child("actualPrice").getValue(Double.class);
                            boolean isActive = snapshot.child("isActive").getValue(Boolean.class);

                            // Create OrderDetail object
                            OrderDetail orderDetail = new OrderDetail(id, null, null, orderId, artId, quantity, actualPrice, isActive);
                            orderDetailList.add(orderDetail);
                            orderDetailAdapter = new OrderDetailAdapter(orderDetailList);
                            recyclerView.setAdapter(orderDetailAdapter);
                            // Notify adapter of data change
                            orderDetailAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e("OrderDetailActivity", "Error parsing order detail: " + e.getMessage());
                        }
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("OrderDetailActivity", "Failed to retrieve order details: " + databaseError.getMessage());
                Toast.makeText(OrderDetailActivity.this, "Failed to retrieve order details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}