package com.example.artshopprm;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SignUp extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, phoneNumberEditText, usernameEditText;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        usernameEditText = findViewById(R.id.editTextUserName);
        signupButton = findViewById(R.id.buttonSignUp);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });
    }

    private void signupUser() {
        // Get user input
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String userName = usernameEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Phone number is required.");
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            usernameEditText.setError("User name is required.");
            return;
        }

        // All input fields are valid, proceed with signing up the user
        Map<String, Object> userMap = new HashMap<>();
        String userId = UUID.randomUUID().toString();  // Generate a unique ID
        userMap.put("id", userId);
        userMap.put("email", email);
        userMap.put("password", password);  // Note: Storing plaintext password is not recommended for production
        userMap.put("phoneNumber", phoneNumber);
        userMap.put("userName", userName);
        userMap.put("role", "USER");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());
        userMap.put("createdDate", currentDate);
        userMap.put("updateDate", currentDate);

        // Save user data to Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference().child("accounts").child(userId)
                .setValue(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignUp.this, "Sign up successful! Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, LoginActivity.class));
                        // Optionally, navigate to the main activity or perform other actions after successful signup
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Failed to sign up. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
