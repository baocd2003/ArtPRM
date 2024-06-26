package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.artshopprm.Adapter.MessageAdapter;
import com.example.artshopprm.Entity.MessageModel;
import com.example.artshopprm.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String recieverId;
    DatabaseReference databaseReferenceSender, databaseReferenceReciever;
    String senderRoom, recieverRoom;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recieverId = getIntent().getStringExtra("id");

        senderRoom = FirebaseAuth.getInstance().getUid() + recieverId;
        recieverRoom = recieverId + FirebaseAuth.getInstance().getUid();

        messageAdapter = new MessageAdapter(this);
        binding.recycler.setAdapter(messageAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReciever = FirebaseDatabase.getInstance().getReference("chats").child(recieverRoom);
        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageAdapter.add(messageModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendMessImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.messageEd.getText().toString();
                if (message.trim().length() > 0) {
                    sendMessages(message);
                }
            }
        });
    }

    private void sendMessages(String message) {
        String messageId = databaseReferenceSender.push().getKey(); // Generate unique key
        MessageModel messageModel = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(), message);
        messageAdapter.add(messageModel);
        databaseReferenceSender
                .child(messageId)
                .setValue(messageModel);
        databaseReferenceReciever
                .child(messageId)
                .setValue(messageModel);
    }

}