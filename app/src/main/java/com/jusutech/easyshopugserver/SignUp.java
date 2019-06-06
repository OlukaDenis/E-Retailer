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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jusutech.easyshopugserver.Common.Common;
import com.jusutech.easyshopugserver.Common.Utils;
import com.jusutech.easyshopugserver.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    private MaterialEditText usrphone, usrname, usrpassword, usremail;
    private Button signup;
    private TextView login;
    private DatabaseReference user_table;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private UserProfileChangeRequest profileUpdates;
    private User user;
    private FirebaseUser firebaseUser;
    ProgressDialog waitingDialog;
    private static final String TAG = SignUp.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        usrphone = (MaterialEditText) findViewById(R.id.met_phone);
        usrname = (MaterialEditText) findViewById(R.id.met_username);
        usrpassword = (MaterialEditText) findViewById(R.id.met_password);
        usremail = (MaterialEditText) findViewById(R.id.met_email);
        signup = (Button) findViewById(R.id.btn_signup);
        login = findViewById(R.id.already_created);

        waitingDialog = new ProgressDialog(this);
        waitingDialog.setTitle("Wait");
        waitingDialog.setMessage("Creating retailer account...");
        //initializing firebase database
        database = FirebaseDatabase.getInstance();
        user_table = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    createUser();

                } else {
                    final AlertDialog noNetworkDialog = new AlertDialog.Builder(SignUp.this)
                            .setCancelable(false)
                            .setTitle("Connection failed")
                            .setMessage("Please check your internet connection")
                            .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    createUser();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    noNetworkDialog.show();
                    //waitingDialog.dismiss();
                    //Toast.makeText(SignupActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }// end of onCreate method

    private void createUser() {
        //checking whether the edit text is empty
        final String  password = usrpassword.getText().toString();
        final String email = usremail.getText().toString();
        final String phone = usrphone.getText().toString().trim();
        final String  name = usrname.getText().toString().trim();
        final String admin = "1";

        final boolean valid_pass = Utils.isValidPassword(password);
        final boolean valid_email = Utils.isValidEmail(email);
        final boolean valid_name = Utils.isValidName(name);
//empty field validations
        if (usrphone.getText().toString().isEmpty()) {
            usrphone.setError("Phone number field must not be empty");
            usrphone.requestFocus();
        } else if (usrname.getText().toString().isEmpty()) {
            usrname.setError("Name field must not be empty");
            usrname.requestFocus();
        } else if (usrpassword.getText().toString().isEmpty()) {
            usrpassword.setError("Password field must not be empty");
            usrpassword.requestFocus();
        } else if (usremail.getText().toString().isEmpty()) {
            usremail.setError("Email field must not be empty");
            usremail.requestFocus();
        }

        //typical validations
        else if (!valid_pass) {
            usrpassword.setError("Password must be at least six characters");
        }
        else if (!valid_email) {
            usremail.setError("Invalid email format");
        }
        else if(!valid_name){
            usrname.setError("Name must not contain symbols or numbers");
        }
        else {
            waitingDialog.show();

            user = new User(name, phone, password, admin);

            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            //Creating a user in firebase authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                firebaseUser = mAuth.getCurrentUser();
                                assert firebaseUser != null;
                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                waitingDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Log.d("REG", "Retailer profile updated.");
                                                    saveCustomerDetails();
                                                }
                                            }
                                        });
                                Log.v(TAG, "Retailer authenticated successfully");
                                //Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                waitingDialog.dismiss();
                                String message = task.getException().getMessage();
                                Log.e(TAG, message);
                                Toast.makeText(SignUp.this, "Retailer account creation failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }//end of else
    }


    //saving the user to the firebase database
    private void saveCustomerDetails() {
        final String userEmail = usremail.getText().toString();
        String userId = Utils.cleanEmailKey(userEmail);
        user_table.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Retailer successfully created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, SignIn.class));
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Retailer account creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveCustomerDisplayDetails(userId);
    }

    private void saveCustomerDisplayDetails(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference saveDetailsDatabase = database.getReference();
        saveDetailsDatabase.child("admin_list").child(userId).setValue(firebaseUser.getDisplayName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {

                        }
                    }
                });
    }


}
