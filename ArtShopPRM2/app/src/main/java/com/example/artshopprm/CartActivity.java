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
import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.Entity.Order;
import com.example.artshopprm.Entity.OrderDetail;
import com.example.artshopprm.Service.ManagementCart;
import com.example.artshopprm.databinding.ActivityCartBinding;
import com.example.artshopprm.databinding.ActivityDetailBinding;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
        binding.totalFeeTxt.setText(String.valueOf(getTotalOrderPrice() + deliveryFee));
        adapter = new CartAdapter(managementCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double total = getTotalOrderPrice();
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


}
