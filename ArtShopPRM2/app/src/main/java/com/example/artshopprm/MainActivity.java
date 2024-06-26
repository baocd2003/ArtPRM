package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Adapter.PopularArtAdapter;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private RecyclerView recyclerViewPopular;
    private PopularArtAdapter popularArtAdapter;
    private List<Art> artList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewPopular = findViewById(R.id.recycleViewPopular);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this));

        artList = new ArrayList<>();
        getArts();
        mainAction();
    }

    private void getArts() {
        DatabaseReference dbRef = db.getReference("arts");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot artSnapshot : dataSnapshot.getChildren()) {
                    Art art = artSnapshot.getValue(Art.class);
                    artList.add(art);
                    // Do something with the art object
                    Log.d("FirebaseData", "Art Name: " + art.getArtName());
                }
                binding.recycleViewPopular.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                popularArtAdapter = new PopularArtAdapter(MainActivity.this, artList);
                recyclerViewPopular.setAdapter(popularArtAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseData", "Failed to read value.", error.toException());
            }
        });

    }

    private void mainAction(){
        binding.imageViewCart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }
}
