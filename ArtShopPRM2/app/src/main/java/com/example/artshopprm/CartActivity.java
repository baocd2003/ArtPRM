package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Adapter.CartAdapter;
import com.example.artshopprm.Api.CreateOrder;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.Entity.Order;
import com.example.artshopprm.Entity.OrderDetail;
import com.example.artshopprm.Service.ManagementCart;
import com.example.artshopprm.databinding.ActivityCartBinding;
import com.example.artshopprm.databinding.ActivityDetailBinding;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends  BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    float deliveryFee = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managementCart = new ManagementCart(this);
        initList();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        binding.PlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.addressTxt.getText().toString().isEmpty() || binding.phoneTxt.getText().toString().isEmpty()){
                    Toast.makeText(CartActivity.this, "Please enter Receiver Address and Phone", Toast.LENGTH_SHORT).show();
                }else{
                    String address = binding.addressTxt.getText().toString();
//                  managementCart.emptyListCart();
                    Intent intent = new Intent(CartActivity.this, OrderPayment.class);
                    intent.putExtra("soluong", "2000");
                    intent.putExtra("address", address);
                    double a = getTotalOrderPrice();
                    intent.putExtra("total", a);
                    startActivity(intent);
                }
            }
        });
    }

    private void initList() {

        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        binding.deliveryTxt.setText(String.valueOf(deliveryFee));
        adapter = new CartAdapter(managementCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double total = managementCart.getTotalFee();
        binding.totalTxt.setText("$" + total);
    }

    public double getTotalOrderPrice(){
        List<Art> arts = managementCart.getListCart();
        double totalPrice = 0;
        for (Art art : arts) {
            String orderDetailId = UUID.randomUUID().toString();
            double actualPrice = art.getNumberInCart() * art.getPrice();
            totalPrice+=actualPrice;
        }
        return totalPrice - deliveryFee;
    }

//    private void actionOrder(){
//        List<Art> arts = managementCart.getListCart();
//        if (arts.isEmpty()) {
//            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        String orderId = UUID.randomUUID().toString();
//        Date now = new Date();
//        String address = binding.addressTxt.getText().toString();
//        // Create an Order object
//        Order order = new Order(orderId, now, now, "BankName", "BankAccount", address, "AccountId", "Pending", true);
//        DatabaseReference ordersRef = db.getReference("orders");
//        DatabaseReference orderDetailsRef = db.getReference("orderDetails");
//
//        // Save order to Firebase
//        ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
//                saveOrderDetails(orderId, arts);
//            } else {
//                Toast.makeText(CartActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
//            }
//        });
//        managementCart.emptyListCart();
//    }
//
//    private void saveOrderDetails(String orderId, List<Art> arts) {
//        DatabaseReference orderDetailsRef = db.getReference("orderDetails");
//        for (Art art : arts) {
//            String orderDetailId = UUID.randomUUID().toString();
//            double actualPrice = art.getNumberInCart() * art.getPrice();
//            OrderDetail orderDetail = new OrderDetail(orderDetailId, new Date(), new Date(),
//                    orderId, art.getId(), art.getNumberInCart(), actualPrice, true);
//            orderDetailsRef.child(orderDetailId).setValue(orderDetail);
//        }
//    }
//
//    private void zaloPayment(String totalString) {
//        CreateOrder orderApi = new CreateOrder();
//        try {
//            JSONObject data = orderApi.createOrder(totalString);
//            String code = data.getString("return_code");
//            if (code.equals("1")) {
//                String token = data.getString("zp_trans_token");
//                ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
//                    @Override
//                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        List<Art> arts = managementCart.getListCart();
//                        Intent intent1 = new Intent(CartActivity.this, PaymentNotification.class);
//                        intent1.putExtra("result", "Thanh toán thành công");
//                        String orderId = UUID.randomUUID().toString();
//                        Date now = new Date();
//                        String address = binding.addressTxt.getText().toString();
//                        // Create an Order object
//                        Order order = new Order(orderId, now, now, "BankName", "BankAccount", address, "AccountId", "Pending", true);
//                        DatabaseReference ordersRef = db.getReference("orders");
//                        DatabaseReference orderDetailsRef = db.getReference("orderDetails");
//
//                        // Save order to Firebase
//                        ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
//                                saveOrderDetails(orderId, arts);
//                            } else {
//                                Toast.makeText(CartActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        managementCart.emptyListCart();
//                        startActivity(intent1);
//                    }
//
//                    @Override
//                    public void onPaymentCanceled(String s, String s1) {
//                        Intent intent1 = new Intent(CartActivity.this, PaymentNotification.class);
//                        intent1.putExtra("result", "Hủy thanh toán");
//                        startActivity(intent1);
//                    }
//
//                    @Override
//                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//                        Intent intent1 = new Intent(CartActivity.this, PaymentNotification.class);
//                        intent1.putExtra("result", "Lỗi thanh toán");
//                        startActivity(intent1);
//                    }
//                });
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
