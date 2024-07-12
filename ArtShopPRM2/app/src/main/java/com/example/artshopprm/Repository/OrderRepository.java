package com.example.artshopprm.Repository;
import com.example.artshopprm.Entity.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
public class OrderRepository {
    private DatabaseReference ordersDatabaseReference;
    private DatabaseReference usersDatabaseReference;

    public OrderRepository() {
        ordersDatabaseReference = FirebaseDatabase.getInstance().getReference("orders");
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference("accounts");
    }

    public void findOrdersByAccountId(String accountId, final OrderCallback callback) {
        ordersDatabaseReference.orderByChild("accountId").equalTo(accountId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Order> orders = new ArrayList<>();
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            orders.add(order);
                        }
                        callback.onOrdersRetrieved(orders);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }

    public void findOrderById(String orderId, final OrderDetailCallback callback) {
        ordersDatabaseReference.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                callback.onOrderRetrieved(order);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void getAccountIdByEmail(String email, final AccountIdCallback callback) {
        usersDatabaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String accountId = userSnapshot.getKey();
                                callback.onAccountIdRetrieved(accountId);
                                return;
                            }
                        }
                        callback.onError(new Exception("User not found"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }

    public interface OrderCallback {
        void onOrdersRetrieved(List<Order> orders);
        void onError(Exception e);
    }

    public interface OrderDetailCallback {
        void onOrderRetrieved(Order order);
        void onError(Exception e);
    }

    public interface AccountIdCallback {
        void onAccountIdRetrieved(String accountId);
        void onError(Exception e);
    }
}
