package com.example.artshopprm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.example.artshopprm.Adapter.CheckoutAdapter;
import com.example.artshopprm.Api.CreateOrder;
import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.Entity.Order;
import com.example.artshopprm.Entity.OrderDetail;
import com.example.artshopprm.Service.ManagementCart;
import com.example.artshopprm.databinding.ActivityCartBinding;
import com.example.artshopprm.databinding.ActivityOrderPaymentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPayment extends BaseActivity {
    TextView txtSoluong, txtTongTien, txtAddress;
    Button btnThanhToan;
    private ActivityOrderPaymentBinding binding;
    private ManagementCart managementCart;
    private FirebaseDatabase db;

    private CheckoutAdapter checkoutAdapter;

    private RecyclerView cartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_payment);
        binding = ActivityOrderPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txtTongTien = findViewById(R.id.textViewTongTien);
        btnThanhToan = findViewById(R.id.buttonThanhToan);
        txtAddress = findViewById(R.id.txtAddress);
        managementCart = new ManagementCart(this);
        db = FirebaseDatabase.getInstance();
        checkoutAdapter = new CheckoutAdapter(managementCart.getListCart(),this);
        cartView = findViewById(R.id.cartView);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        Intent intent = getIntent();
        Double total = intent.getDoubleExtra("total", (double) 0);
        String totalString = String.format("%.0f", total);
        txtTongTien.setText(Double.toString(total));
        txtAddress.setText(intent.getStringExtra("address"));
        displayCart();
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(totalString);
                    String code = data.getString("return_code");
                    if (code.equals("1")) {
                        String token = data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder(OrderPayment.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                Log.d("Payment", "Payment succeeded");
                                actionOrder();
                                Intent intent1 = new Intent(OrderPayment.this, PaymentNotification.class);
                                intent1.putExtra("result", "Thanh toán thành công");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Intent intent1 = new Intent(OrderPayment.this, PaymentNotification.class);
                                intent1.putExtra("result", "Hủy thanh toán");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Intent intent1 = new Intent(OrderPayment.this, PaymentNotification.class);
                                intent1.putExtra("result", "Lỗi thanh toán");
                                startActivity(intent1);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void displayCart(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartView.setLayoutManager(linearLayoutManager);
        cartView.setAdapter(checkoutAdapter);
    }

    private void actionOrder() {
        List<Art> arts = managementCart.getListCart();
        if (arts.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Account acc = managementCart.getUserLogined("User");
        String orderId = UUID.randomUUID().toString();
        Date now = new Date();
        double total = Double.parseDouble(txtTongTien.getText().toString());
        String address = txtAddress.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        String currentTime = dateFormat.format(now);
          // Update with actual address fetching logic
        // Create an Order object
        Order order = new Order(orderId, currentTime, currentTime, address, String.valueOf(acc.getId()), "Pending", true,total);
        DatabaseReference ordersRef = db.getReference("orders");
        DatabaseReference orderDetailsRef = db.getReference("orderDetails");

        // Save order to Firebase
        ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(OrderPayment.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                Log.d("Order", "Order placed successfully");
                saveOrderDetails(orderId, arts);
            } else {
                Toast.makeText(OrderPayment.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                Log.d("Order", "Failed to place order");
            }
        });
        managementCart.emptyListCart();
    }

    private void saveOrderDetails(String orderId, List<Art> arts) {
        DatabaseReference orderDetailsRef = db.getReference("orderDetails");
        DatabaseReference artsRef = db.getReference("arts");
        for (Art art : arts) {
            String orderDetailId = UUID.randomUUID().toString();
            double actualPrice = art.getNumberInCart() * art.getPrice();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            String currentTime = dateFormat.format(now);
            OrderDetail orderDetail = new OrderDetail(orderDetailId, currentTime, currentTime,
                    orderId, art.getId(), art.getNumberInCart(), actualPrice, true);
            orderDetailsRef.child(orderDetailId).setValue(orderDetail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("OrderDetail", "OrderDetail placed successfully");
                } else {
                    Log.d("OrderDetail", "Failed to place OrderDetail");
                }
            });
            int updatedStock = art.getStockQuantity() - art.getNumberInCart();
            art.setStockQuantity(updatedStock);
            artsRef.child(art.getId()).setValue(art).addOnCompleteListener(stockUpdateTask -> {
                if (stockUpdateTask.isSuccessful()) {
                    Log.d("StockUpdate", "Stock updated successfully for art ID: " + art.getId());
                } else {
                    Log.d("StockUpdate", "Failed to update stock for art ID: " + art.getId());
                }
            });
        }
    }
}