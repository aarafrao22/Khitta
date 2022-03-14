package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends Activity {

    private EditText nameField,
            emailField,
            passwordField,
            phoneNoField;
    private Button signupBtn,btnEMail,btnPhoneNumber;
    private LinearLayout loginScreeBtn;

    private String userName, userEmail, userPassword, userPhoneno;

    boolean netConnection;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        checkConnection();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUserSignUp();
            }
        });

        loginScreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginScreen = new Intent(SignUp.this,Login.class);
                startActivity(loginScreen);
                finish();
            }
        });
    }

    private void newUserSignUp()
    {
        //userName=nameField.getText().toString().trim();
        userEmail=emailField.getText().toString().trim();
        userPassword=passwordField.getText().toString().trim();
       // userPhoneno=phoneNoField.getText().toString().trim();


//            if (userName.equals("")) {
//                nameField.setError("Name required!");
//            }
             if (userEmail.equals("")) {
                emailField.setError("Email required!");
            } else if (userPassword.equals("")) {
                passwordField.setError("Password Required!");
            } else if (userPassword.length() < 6) {
                passwordField.setError("At least 6 character password");
            } else if ((!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())) {
                emailField.setError("Wrong format");
            }
//            else if (userPhoneno.equals("")) {
//                phoneNoField.setError("Phone Number Required!");
//            }
//            else if (userPhoneno.length() < 11) {
//                phoneNoField.setError("Wrong Number");
//            }
            else if (netConnection){

                //  new user registration
                mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener
                                    (new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String currentUserId = mAuth.getCurrentUser().getUid();
                                    HashMap<String,String> myData = new HashMap<String,String>();
                                    myData.put("ID",currentUserId);
                                   // myData.put("Name",userName);
                                    myData.put("Email",userEmail);
                                    myData.put("password",userPassword);
                                   // myData.put("PhoneNo",userPhoneno);

                                    databaseReference.child("users").child(currentUserId).setValue(myData)
                                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SignUp.this, "Registered Successfully.Please check your email for verification.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });

                            startActivity(new Intent(SignUp.this,Login.class));
                            Toast.makeText(SignUp.this, "Welcome :"+ userName, Toast.LENGTH_SHORT).show();
                            finish();

                        }else{
                            String error = task.getException().toString();
                            Toast.makeText(SignUp.this, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }
        else{

            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }}

    private void checkConnection() {
        netConnection=false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            netConnection = true;
        }
        else{
            netConnection = false;
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
      //  nameField = findViewById(R.id.Edt_Name);
        emailField = findViewById(R.id.Edt_Emial);
        passwordField = findViewById(R.id.Edt_Password);
       // phoneNoField = findViewById(R.id.Edt_PhoneNo);
        signupBtn = findViewById(R.id.btnSubmit);
        loginScreeBtn =findViewById(R.id.gotoSignin);
        btnEMail=findViewById(R.id.btnEmail);
        btnPhoneNumber=findViewById(R.id.btnPhoneNumber);

        btnEMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(SignUp.this,SignUp.class));
               finish();
            }
        });

        btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(SignUp.this,SignUpWithPhone.class));
              finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUp.this,MainActivity.class));
        finish();
    }
}