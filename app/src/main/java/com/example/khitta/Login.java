package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity {

    EditText edtEmail,edtPaswword;
    Button login;
    LinearLayout gotoSignup;
    private String strEmail,strPassword;
    boolean netConnection;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // if user verified his email then we will send him to profile activity//
//        if (firebaseUser !=null && firebaseUser.isEmailVerified()){
//            startActivity(new Intent(Login.this,UserProfile.class));
//        }
        init();
        checkConnection();
        mAuth = FirebaseAuth.getInstance();

        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

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
        }
    }

    private void init()
    {

        edtEmail=findViewById(R.id.Edt_Emial);
        edtPaswword=findViewById(R.id.Edt_Password);
        login=findViewById(R.id.btnLogin);
        gotoSignup=findViewById(R.id.gotoSignup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
    private void loginUser() {
        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPaswword.getText().toString().trim();

        if (strEmail.isEmpty()) {
            edtEmail.setError("Email Required");
        } else if (strPassword.isEmpty()) {
            edtPaswword.setError("password Required");
        } else if (strPassword.length() < 6) {
            edtPaswword.setError("At least 6 character password");
        } else if ((!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            edtEmail.setError("Wrong format");
        } else if (netConnection) {

            mAuth.signInWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(Login.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    startActivity(new Intent(Login.this,UserProfile.class));
                                    finish();
                                }else {
                                    Toast.makeText(Login.this, "Please verify your email address.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}