package com.example.artshopprm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.artshopprm.Adapter.BannerAdapter;
import com.example.artshopprm.Adapter.PopularArtAdapter;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private RecyclerView recyclerViewPopular;
    private RecyclerView recyclerViewNewest;
    private PopularArtAdapter popularArtAdapter;
    private List<Art> artList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewPopular = findViewById(R.id.recycleViewPopular);
        recyclerViewNewest = findViewById(R.id.recycleViewNewest);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNewest.setLayoutManager(new LinearLayoutManager(this));
        artList = new ArrayList<>();
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        BannerAdapter bannerAdapter = new BannerAdapter(Arrays.asList(
                R.drawable.wallpaper_1,
                R.drawable.wallpaper_2,
                R.drawable.wallpaper_3
        ));
        viewPager.setAdapter(bannerAdapter);
        getArts();
        getNewestArts();
        mainAction();
        checkAuthentication();
        // Listen for Enter key press
        EditText editText = findViewById(R.id.editTextText);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                String searchTerm = editText.getText().toString().trim();
                if (!searchTerm.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("SEARCH_TERM", searchTerm);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        });
    }
    private void checkAuthentication() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        if (email == null || password == null) {
            // If user data is not stored in SharedPreferences, navigate to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); // Close MainActivity
        }
    }

    private void getArts() {
        DatabaseReference dbRef = db.getReference("arts");
        binding.progressBarPopularArts.setVisibility(View.VISIBLE);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot artSnapshot : dataSnapshot.getChildren()) {
                    Art art = artSnapshot.getValue(Art.class);
                    if(art.getStockQuantity() > 0){
                        artList.add(art);
                    }

                    // Do something with the art object
                    Log.d("FirebaseData", "Art Name: " + art.getArtName());
                }
                binding.recycleViewPopular.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                // Sort artList by descending rate
                Collections.sort(artList, new Comparator<Art>() {
                    @Override
                    public int compare(Art art1, Art art2) {
                        // Sort in descending order of rate
                        return Float.compare(art2.getRate(), art1.getRate());
                    }
                });
                popularArtAdapter = new PopularArtAdapter(MainActivity.this, artList);
                recyclerViewPopular.setAdapter(popularArtAdapter);
                binding.progressBarPopularArts.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseData", "Failed to read value.", error.toException());
            }
        });

    }

    private void getNewestArts() {
        DatabaseReference dbRef = db.getReference("arts");
        binding.progressBarPopularArts.setVisibility(View.VISIBLE);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear artList to avoid duplicates
                artList.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot artSnapshot : dataSnapshot.getChildren()) {
                    Art art = artSnapshot.getValue(Art.class);
                    if(art.getStockQuantity() > 0){
                        artList.add(art);
                    }
                    // Do something with the art object
                    Log.d("FirebaseData", "Art Name: " + art.getArtName());
                }
                binding.recycleViewNewest.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                // Sort artList by descending rate
                Collections.sort(artList, new Comparator<Art>() {
                    @Override
                    public int compare(Art art1, Art art2) {
                        // Sort in descending order of rate
                        return art2.getCreatedDateAsDate().compareTo(art1.getCreatedDateAsDate());
                    }
                });
                popularArtAdapter = new PopularArtAdapter(MainActivity.this, artList);
                recyclerViewNewest.setAdapter(popularArtAdapter);
                binding.progressBarPopularArts.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseData", "Failed to read value.", error.toException());
            }
        });

    }

    private void mainAction(){
        binding.imageViewCart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        binding.imageViewChat.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListUserActivity.class)));
        binding.imageViewLocation.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapActivity.class)));
        binding.imageViewAccount.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListOrderActivity.class)));
    }
}
