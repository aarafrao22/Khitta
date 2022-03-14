package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ContactUs extends Activity {
    EditText mNAme,mEmail,mSubject,mMessage;
    Button mBtnSend;
    DatabaseReference dbRef;
    FirebaseUser mAuth;
    String StringName,StringEmail,StringSubject,StringMessage;
    ImageButton backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        dbRef=FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance().getCurrentUser();
        mNAme=findViewById(R.id.edtName);
        mEmail=findViewById(R.id.edtEmail);
        mSubject=findViewById(R.id.edtSubject);
        mMessage=findViewById(R.id.edtMessage);
        mBtnSend=findViewById(R.id.btnSend);
        backArrow=findViewById(R.id.backArrow);

        //==back press on arrow in tolbar
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backarrow=new Intent(ContactUs.this,UserProfile.class);
                startActivity(backarrow);
                finish();
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData();
            }
        });
    }

    private void FormData() {

        StringName=mNAme.getText().toString();
        StringEmail=mEmail.getText().toString();
        StringSubject=mSubject.getText().toString();;
        StringMessage=mMessage.getText().toString();

        if (StringName.isEmpty()){
            mNAme.setError("Enter your name!!");
        }
        else if(StringEmail.isEmpty()) {
            mEmail.setError("Enter Subject!!");
        }
        else if(StringSubject.isEmpty()){
            mSubject.setError("Enter Subject!!");
        }
        else if(StringMessage.isEmpty()){
            mMessage.setError("Enter Message!!");
        }
        else if(!StringMessage.equals("")){
            final HashMap<String,String> contactData=new HashMap<>();
            String UID=mAuth.getUid();
            contactData.put("cusName",StringName);
            contactData.put("cusEmail",StringEmail);
            contactData.put("cusSubject",StringSubject);
            contactData.put("cusMessage",StringMessage);
            contactData.put("cusID",UID);

            dbRef.child("Contact Us Data").child(UID).setValue(contactData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ContactUs.this, "Your Message has been send!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ContactUs.this,UserProfile.class));
                                        finish();
                                    }
                                }
                            });
        }
        else {
            Toast.makeText(this, "please try again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    //back press button code...
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(ContactUs.this,UserProfile.class));
        finish();
    }
}