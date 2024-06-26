package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.artshopprm.Adapter.UserAdapter;
import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.databinding.ActivityListUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListUserActivity extends AppCompatActivity {
    UserAdapter userAdapter;
    ActivityListUserBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String currentUserId = FirebaseAuth.getInstance().getUid();
        userAdapter = new UserAdapter(this, currentUserId);
        binding.recycler.setAdapter(userAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("accounts");

        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(ListUserActivity.this, MainActivity.class)));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    Account userModel = dataSnapshot.getValue(Account.class);
                    if (userModel != null && !uid.equals(currentUserId)) {
                        userAdapter.add(userModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ListUserActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
