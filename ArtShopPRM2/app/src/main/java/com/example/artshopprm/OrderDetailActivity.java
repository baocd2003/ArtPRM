package com.example.artshopprm;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artshopprm.Entity.OrderDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderIdTextView;
    private TextView artNameTextView;
    private TextView artDescriptionTextView; // New TextView for art description
    private TextView rateTextView; // New TextView for rate
    private TextView authorTextView; // New TextView for author
    private TextView quantityTextView;
    private TextView actualPriceTextView;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderIdTextView = findViewById(R.id.orderIdTextView);
        artNameTextView = findViewById(R.id.artNameTextView);
        artDescriptionTextView = findViewById(R.id.artDescriptionTextView); // Initialize artDescriptionTextView
        rateTextView = findViewById(R.id.rateTextView); // Initialize rateTextView
        authorTextView = findViewById(R.id.authorTextView); // Initialize authorTextView
        quantityTextView = findViewById(R.id.quantityTextView);
        actualPriceTextView = findViewById(R.id.actualPriceTextView);

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
        }

        if (orderId != null) {
            retrieveOrderDetailsFromFirebase(orderId);
        } else {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void retrieveOrderDetailsFromFirebase(String orderId) {
        DatabaseReference orderDetailsRef = FirebaseDatabase.getInstance().getReference("orderDetails");

        // Query to retrieve order details by orderId
        orderDetailsRef.orderByChild("orderId").equalTo(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Ensure there is data
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            String id = snapshot.child("id").getValue(String.class);
                            String orderId = snapshot.child("orderId").getValue(String.class);
                            String artId = snapshot.child("artId").getValue(String.class);
                            Integer quantity = snapshot.child("quantity").getValue(Integer.class);
                            Double actualPrice = snapshot.child("actualPrice").getValue(Double.class);
                            Boolean isActive = snapshot.child("isActive").getValue(Boolean.class);

                            // Now create OrderDetail object with fetched details except artName
                            OrderDetail orderDetail = new OrderDetail(id, null, null, orderId, artId, quantity != null ? quantity : 0, actualPrice != null ? actualPrice : 0.0, isActive != null && isActive);

                            // Fetch and display art details
                            retrieveAndDisplayArtDetails(orderDetail);

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

    // Method to retrieve art details and display them
    private void retrieveAndDisplayArtDetails(OrderDetail orderDetail) {
        DatabaseReference artRef = FirebaseDatabase.getInstance().getReference("arts").child(orderDetail.getArtId());

        artRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String artName = dataSnapshot.child("artName").getValue(String.class);
                    String artDescription = dataSnapshot.child("description").getValue(String.class);
                    int rate = dataSnapshot.child("rate").getValue(Integer.class);
                    String author = dataSnapshot.child("author").getValue(String.class);

                    // Set art details to TextViews
                    artNameTextView.setText("Art Name: " + artName);
                    artDescriptionTextView.setText("Description: " + artDescription);
                    rateTextView.setText("Rate: " + rate);
                    authorTextView.setText("Author: " + author);

                    // Display order details
                    displayOrderDetail(orderDetail);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Art not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("OrderDetailActivity", "Failed to retrieve art details: " + databaseError.getMessage());
                Toast.makeText(OrderDetailActivity.this, "Failed to retrieve art details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrderDetail(OrderDetail orderDetail) {
        orderIdTextView.setText("Order ID: " + orderDetail.getOrderId());
        quantityTextView.setText("Quantity: " + orderDetail.getQuantity());
        actualPriceTextView.setText("Actual Price: $" + orderDetail.getActualPrice());
    }
}
