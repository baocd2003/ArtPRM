package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.example.artshopprm.Adapter.PopularArtAdapter;
import com.example.artshopprm.Entity.Art;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PopularArtAdapter popularArtAdapter;
    private List<Art> artList;
    private ImageView backBtn; // ImageView for back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        backBtn = findViewById(R.id.backtoMainBtn);

        // Set click listener for back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        artList = new ArrayList<>();

        // Retrieve search term from Intent
        String searchTerm = getIntent().getStringExtra("SEARCH_TERM");

        // Firebase Database reference
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("arts");

        // Query to filter arts based on search term
        Query searchQuery = dbRef.orderByChild("artName").startAt(searchTerm).endAt(searchTerm + "\uf8ff");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artList.clear();
                for (DataSnapshot artSnapshot : dataSnapshot.getChildren()) {
                    Art art = artSnapshot.getValue(Art.class);
                    if (art != null && art.getStockQuantity() > 0 && art.getArtName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        artList.add(art);
                    }
                }
                // Update RecyclerView with filtered data using PopularArtAdapter
                popularArtAdapter = new PopularArtAdapter(SearchResultActivity.this, artList);
                recyclerView.setAdapter(popularArtAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error fetching data", databaseError.toException());
            }
        });
    }
}
