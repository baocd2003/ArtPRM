package com.example.artshopprm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.Service.ManagementCart;
import com.example.artshopprm.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Art art;
    private int num =1;
    private ManagementCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        setArt();
        actionCart();
    }

    private void setArt(){
        managementCart = new ManagementCart(this);
        art = (Art) getIntent().getSerializableExtra("art");
        binding.backBtn.setOnClickListener(v -> finish());
        binding.priceTxt.setText("$" + art.getPrice());
        binding.titleTxt.setText(art.getArtName());
        binding.descriptionTxt.setText(art.getDescription());
        binding.rateTxt.setText(art.getRate() + " Rating");
        binding.ratingBar.setRating((float)art.getRate());
        binding.totalTxt.setText((num * art.getPrice())  + "$");
        binding.stockTxt.setText(art.getStockQuantity() + "left");
        Picasso.get().load(art.getImageUrl()).into(binding.pic);
    }

    private void actionCart(){
        binding.plusBtn.setOnClickListener(v -> {
            if(num >= art.getStockQuantity()){
                Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show();
            }else{
                num = num + 1;
                binding.numTxt.setText(num + " ");
                binding.totalTxt.setText("$" + (num * art.getPrice()));
            }
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText("$" + (num * art.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            art.setNumberInCart(num);
            managementCart.insertFood(art);
        });
    }

}