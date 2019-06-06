package com.jusutech.easyshopugserver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.Common.Utils;
import com.jusutech.easyshopugserver.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;

public class SignIn extends AppCompatActivity {
    EditText email, password;
    Button login;
    TextView create, forgot_password;
    LinearLayout login_layout;
    DatabaseReference user_table;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog waitingDialog;
    User user;

    private static final String TAG = SignIn.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //getting the reference
        email = (MaterialEditText) findViewById(R.id.email);
        password = (MaterialEditText) findViewById(R.id.password);
        login = findViewById(R.id.login);
        create = findViewById(R.id.no_account);
        login_layout = findViewById(R.id.login_layout);

        waitingDialog = new ProgressDialog(this);
        waitingDialog.setTitle("Wait");
        waitingDialog.setMessage("Signing you in...");

        //initializing firebase database
        database = FirebaseDatabase.getInstance();
        user_table = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connection
                if (Common.isNetworkAvailable(getBaseContext())) {
                    loginUser();
                } else {
                    final AlertDialog noNetworkDialog = new AlertDialog.Builder(SignIn.this)
                            .setCancelable(false)
                            .setTitle("Connection failed")
                            .setMessage("Please check your internet connection")
                            .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loginUser();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    noNetworkDialog.show();
                    waitingDialog.dismiss();
                    //SnackBar.make(login_layout, "Check your internet connection", SnackBar.LENGTH_SHORT).show();
                    // Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        final String userEmail = email.getText().toString();
        final String pass = password.getText().toString();
        final boolean valid_pass = Utils.isValidPassword(pass);
        final boolean valid_email = Utils.isValidEmail(userEmail);

        //Save user and password
        //Paper.book().write(Common.USER_KEY, email);
        //Paper.book().write(Common.PASSWORD_KEY, pass);
        //checking whether the edit text is empty
        if (userEmail.isEmpty()) {
            email.setError("You must fill in the phone number!");
            email.requestFocus();
        } else if (pass.isEmpty()) {
            password.setError("You must fill in the password!");
            password.requestFocus();
        }

        //typical validations
        else if (!valid_pass) {
            password.setError("Short password");
        }
        else if (!valid_email) {
            email.setError("Invalid email format");
        }
        //If the textfields are not empty
        else {

            //save current user's details
            final String cleanEmail = Utils.cleanEmailKey(userEmail);
            //getCurrentUserDetails(cleanEmail);

            //setting a dialog to tell the user to wait
            waitingDialog.show();
            //signing the user to the firebase authentication
            user_table.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(cleanEmail).exists()){
                      //  mDialog.dismiss();
                        User user = dataSnapshot.child(cleanEmail).getValue(User.class);
                        assert user != null;
                        user.setEmail(cleanEmail);
                       // final String admi = "true";
                        if(user.getAdmin().equals("1")){
                            if(user.getPassword().equals(pass))
                            {
                                Intent login = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(login);
                                finish();
                            }
                            else{
                                Toast.makeText(SignIn.this,"Wrong password", Toast.LENGTH_SHORT).show();
                                waitingDialog.dismiss();
                            }

                        }
                        else{
                            Toast.makeText(SignIn.this,"Please login with retailer account", Toast.LENGTH_SHORT).show();
                            waitingDialog.dismiss();
                        }

                    }
                    else{
                        waitingDialog.dismiss();
                        Toast.makeText(SignIn.this,"Retailer doesn't exist", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }//end of else if
    }


}
