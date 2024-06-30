package com.example.artshopprm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.Service.ManagementCart;
import com.example.artshopprm.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private ManagementCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managementCart = new ManagementCart(this);
        setVariable();
        MoveToSignUpScreen();
    }



    private void setVariable() {
        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            CheckLogin(email, password);
        });
    }

    private void MoveToSignUpScreen() {
        binding.buttonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUp.class));
        });
    }

    private Task<ArrayList<Account>> GetDataFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("accounts");
        TaskCompletionSource<ArrayList<Account>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Account> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(dataSnapshot.getValue(Account.class));
                    }
                }
                taskCompletionSource.setResult(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    private void CheckLogin(String email, String password) {
        GetDataFromFirebase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Account> list = task.getResult();
                boolean isValidUser = false;
                Account userData = new Account();
                for (Account user : list) {
                    // Kiểm tra thông tin đăng nhập với dữ liệu từ Firebase
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                        isValidUser = true;
                        userData = user;
                        managementCart.setUserLogin(userData);
                        break;
                    }
                }
                if (isValidUser) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id", userData.getId());
                    editor.putString("email", userData.getEmail());
                    editor.putString("password", userData.getPassword()); // Note: Storing passwords directly is not recommended
                    editor.putString("phoneNumber", userData.getPhoneNumber());
                    editor.putString("userName", userData.getUserName());
                    editor.putString("role", userData.getRole());
                    editor.putString("createdDate", userData.getCreatedDate());
                    editor.putString("updateDate", userData.getUpdateDate());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish(); // Close LoginActivity
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Username or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Error when reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
