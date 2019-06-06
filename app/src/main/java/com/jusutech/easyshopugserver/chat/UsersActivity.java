package com.jusutech.easyshopugserver.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jusutech.easyshopugserver.Interface.ItemClickListener;
import com.jusutech.easyshopugserver.Model.User;
import com.jusutech.easyshopugserver.R;
import com.jusutech.easyshopugserver.ViewHolder.UserViewHolder;


import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView userRecycleView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference users_list;
    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;
    FirebaseUser firebaseUser;

    List<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setTitle("Retailer");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase
        database = FirebaseDatabase.getInstance();
        users_list = database.getReference("User");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //init
        userRecycleView = (RecyclerView) findViewById(R.id.rv_users_list);
        userRecycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userRecycleView.setLayoutManager(layoutManager);

        loadUsersList();
    }

    private void loadUsersList() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.layout_users,
                UserViewHolder.class,
                users_list.orderByChild("admin").equalTo("0")
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, final User model, int position) {
                list.add(model);

                viewHolder.username.setText(model.getName());
                //Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.profile_pic);


                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent messageIntent = new Intent(UsersActivity.this, MessageActivity.class);
                        messageIntent.putExtra("userId", adapter.getRef(position).getKey());
                        startActivity(messageIntent);
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        userRecycleView.setAdapter(adapter);
    }
}
