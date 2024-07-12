package com.example.artshopprm.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.artshopprm.Repository.OrderRepository;

import java.util.List;

public class OrderService {
    private OrderRepository orderRepository;
    private Context context;

    public OrderService(OrderRepository orderRepository, Context context) {
        this.orderRepository = orderRepository;
        this.context = context;
    }

    public void getOrdersForLoggedInUser(final OrderRepository.OrderCallback callback) {
        String email = getLoggedInUserEmail();
        orderRepository.getAccountIdByEmail(email, new OrderRepository.AccountIdCallback() {
            @Override
            public void onAccountIdRetrieved(String accountId) {
                orderRepository.findOrdersByAccountId(accountId, callback);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getOrderById(String orderId, final OrderRepository.OrderDetailCallback callback) {
        orderRepository.findOrderById(orderId, callback);
    }

    private String getLoggedInUserEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }
}