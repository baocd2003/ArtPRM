package com.example.artshopprm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Adapter.OrderAdapter;
import com.example.artshopprm.Entity.Order;
import com.example.artshopprm.Repository.OrderRepository;
import com.example.artshopprm.Service.OrderService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ListOrderActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderService = new OrderService(new OrderRepository(), this);

        // Retrieve orders from Firebase
        retrieveOrdersFromFirebase();
    }

    private Order parseOrder(DataSnapshot snapshot) {
        Order order = new Order();
        try {
            String id = snapshot.child("id").getValue(String.class);
            String createdDate = snapshot.child("createdDate").getValue(String.class);
            String updateDate = snapshot.child("updateDate").getValue(String.class);
            String bankName = snapshot.child("bankName").getValue(String.class);
            String bankAccount = snapshot.child("bankAccount").getValue(String.class);
            String deliveryAddress = snapshot.child("deliveryAddress").getValue(String.class);
            String accountId = snapshot.child("accountId").getValue(String.class);
            String status = snapshot.child("status").getValue(String.class);

            // Handle nullable fields
            Boolean isActiveObj = snapshot.child("isActive").getValue(Boolean.class);
            boolean isActive = isActiveObj != null && isActiveObj; // Default to false if isActiveObj is null

            order = new Order(id, createdDate, updateDate, bankName, bankAccount, deliveryAddress, accountId, status, isActive);
        } catch (Exception e) {
            Log.e("ListOrderActivity", "Error parsing order: " + e.getMessage());
        }
        return order;
    }


    private void retrieveOrdersFromFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accountId = sharedPreferences.getString("id", null);
        if (accountId == null) {
            // Handle case where id is not found in SharedPreferences
            Log.e("ListOrderActivity", "Account ID not found in SharedPreferences");
            return;
        }

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        Query query = ordersRef.orderByChild("accountId").equalTo(accountId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        orders.add(order);
                    }
                }
                displayOrders(orders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ListOrderActivity", "Failed to retrieve orders: " + databaseError.getMessage());
                Toast.makeText(ListOrderActivity.this, "Failed to retrieve orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrders(List<Order> orders) {
        orderAdapter = new OrderAdapter(orders, this);
        ordersRecyclerView.setAdapter(orderAdapter);
    }
}
