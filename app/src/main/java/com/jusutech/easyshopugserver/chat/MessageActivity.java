package com.jusutech.easyshopugserver.chat;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.Common.Utils;
import com.jusutech.easyshopugserver.Model.Chat;
import com.jusutech.easyshopugserver.Model.User;
import com.jusutech.easyshopugserver.R;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jusutech.easyshopugserver.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    ImageView profile_pic;
    TextView username;
    ImageView callUser;

    ImageButton sendMessage;
    EditText textMessage;

    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    DatabaseReference currentUser;
    DatabaseReference chatReference;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> chatList;
    RecyclerView recyclerView;

    String currUsr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_pic = (ImageView) findViewById(R.id.img_profile);
        username = (TextView) findViewById(R.id.tv_message_username);
        callUser = findViewById(R.id.img_call_user);
        sendMessage = (ImageButton) findViewById(R.id.btn_send) ;
        textMessage = (EditText) findViewById(R.id.text_send);

        currUsr = Utils.cleanEmailKey(Common.currentUser.getEmail());

        //init
        recyclerView = (RecyclerView) findViewById(R.id.text_message_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        intent = getIntent();
        final String userId = intent.getStringExtra("userId");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        currentUser = database.getReference("User").child(userId);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textMessage.getText().toString();
                if(!msg.equals("")){
                    //sendMessage(firebaseUser.getUid(), userId, msg);
                    sendMessage(currUsr, userId, msg);
                }
                else {
                    Toast.makeText(MessageActivity.this, "You cant send an empty message", Toast.LENGTH_SHORT).show();
                }

                textMessage.setText("");
            }
        });


        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getName());
                final String phonenumber = user.getPhone();
                callUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:"+ phonenumber));
                        startActivity(phoneIntent);
                    }
                });

                //readMessage(firebaseUser.getUid(), userId);
                readMessage(currUsr, userId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference chatReference   = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        chatReference.child("Chats").push().setValue(hashMap);

    }


    private void readMessage(final String myId, final String userId){
        chatList = new ArrayList<>();

        chatReference = FirebaseDatabase.getInstance().getReference("Chats");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        chatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, chatList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

