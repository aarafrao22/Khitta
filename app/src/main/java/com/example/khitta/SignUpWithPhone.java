package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpWithPhone extends AppCompatActivity {

    EditText phoneNumber,enterOTPField;
    TextView countryCode;
    Button sendOTPBtn,verifyOTP,resendOTP;
    String userPhoneNumber,verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_phone);

        fAuth=FirebaseAuth.getInstance();
        countryCode=findViewById(R.id.cCode);
        phoneNumber=findViewById(R.id.editTextPhone6);
        sendOTPBtn=findViewById(R.id.btnSendOtp);

        enterOTPField=findViewById(R.id.enterOTPField);
        verifyOTP=findViewById(R.id.verifyOtpBtn);
        resendOTP=findViewById(R.id.resendOtpBtn);
        resendOTP.setEnabled(false);

        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCode.getText().toString().isEmpty()){
                    countryCode.setError("Required!!");
                    return;
                }

                if (phoneNumber.getText().toString().isEmpty()){
                    phoneNumber.setError("Phone number is required.");
                    return;
                }
                Toast.makeText(SignUpWithPhone.this, "Sending OTP Please Wait.", Toast.LENGTH_SHORT).show();
                userPhoneNumber="+"+countryCode.getText().toString()+phoneNumber.getText().toString();
                verifyPhoneNumber(userPhoneNumber);
            }
        });

        //===verifyOTP Button clickListener here===//
        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the otp
                if (enterOTPField.getText().toString().isEmpty()){
                    enterOTPField.setError("Enter OTP First");
                    return;
                }else {
                    startActivity(new Intent(SignUpWithPhone.this,ProfileSetting.class));
                    finish();
                }
                PhoneAuthCredential credential=PhoneAuthProvider
                        .getCredential(verificationId,enterOTPField.getText().toString());
                authenticateUser(credential);
            }
        });

        //===verifyOTP Button clickListener here===//
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumber(userPhoneNumber);
                resendOTP.setEnabled(false);
            }
        });
        callbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                authenticateUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(SignUpWithPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId=s;
                token=forceResendingToken;

                //hide all widgets which is already visible
                countryCode.setVisibility(View.GONE);
                phoneNumber.setVisibility(View.GONE);
                sendOTPBtn.setVisibility(View.GONE);

                //visible the widgets which is hide
                enterOTPField.setVisibility(View.VISIBLE);
                verifyOTP.setVisibility(View.VISIBLE);
                resendOTP.setVisibility(View.VISIBLE);
                resendOTP.setEnabled(false);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendOTP.setEnabled(true);
            }
        };

    }

    public void verifyPhoneNumber(String phoneNum)
    {
        //send the OTP
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(fAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void authenticateUser(PhoneAuthCredential credential)
    {
        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignUpWithPhone.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpWithPhone.this,UserProfile.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpWithPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpWithPhone.this,SignUp.class));
        finish();
    }
}