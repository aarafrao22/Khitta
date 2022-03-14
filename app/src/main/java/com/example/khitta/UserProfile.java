package com.example.khitta;


import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khitta.Models.UserInfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserProfile extends Activity {
    ImageView profileImage;
    TextView profileName,profileEmail,profilePhoneNo,profileGender,profileAddress,
            profileFName,profileDOB,profileCnic;
    LinearLayout profileSetting,profileMyProperty,profileMyFavorites,
            profileContactUs,profileInvite,profilePrivacyPolicy,profileLogOut;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialization();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        getUserInfo();
    }

    private void getUserInfo()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            DatabaseReference databaseReference=firebaseDatabase.getReference()
                    .child("User Profile Info").child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserInfoModel model=snapshot.getValue(UserInfoModel.class);

                    //  Glide.with(context).load(model.getImagePath()).into(profileImage);
                    if(snapshot.exists()) {
                        assert model != null;
                        profileName.setText(model.getProfileName());
                        profileEmail.setText(model.getProfileEmail());
                        profilePhoneNo.setText(model.getProfilePhoneNo());
                        profileGender.setText(model.getProfileGender());
                        profileAddress.setText(model.getProfileAddress());
                        profileFName.setText(model.getProfileFatherName());
                        profileDOB.setText(model.getProfileDOB());
                        profileCnic.setText(model.getProfileCnic());
                    }else {
                        Toast.makeText(UserProfile.this, "No Record Available.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserProfile.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void initialization()
    {
        profileSetting=findViewById(R.id.profileSetting);
        profileMyProperty=findViewById(R.id.profileProperties);
        profileMyFavorites=findViewById(R.id.profileFavorites);
        profileContactUs=findViewById(R.id.profileContactUs);
        profileInvite=findViewById(R.id.profileInvite);
        profilePrivacyPolicy=findViewById(R.id.profilePrivacyPolicy);
        profileLogOut=findViewById(R.id.profileLogOut);

        //profile
        profileImage=findViewById(R.id.profileImage);
        profileName=findViewById(R.id.profileName);
        profileEmail=findViewById(R.id.profileEmail);
        profilePhoneNo=findViewById(R.id.profilePhoneNo);
        profileGender=findViewById(R.id.profileGender);
        profileAddress=findViewById(R.id.profileAddress);
        profileFName=findViewById(R.id.profileFName);
        profileDOB=findViewById(R.id.profileDOB);
        profileCnic=findViewById(R.id.profileCNIC);

        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });

        profileMyProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMyProperties();
            }
        });

        profileMyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMyFavorites();
            }
        });

        profileContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactUsMethod();
            }
        });

        profileInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InviteFriendsMethod();
            }
        });
    }

    //==Update profile code ==//
    private void UpdateProfile() {
        final DialogPlus dialogPlus = DialogPlus.newDialog(UserProfile.this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.profile_update_popup))
                .setExpanded(true, 1400)
                .create();

        View view = dialogPlus.getHolderView();
        EditText Name = view.findViewById(R.id.edtName);
        EditText Email = view.findViewById(R.id.edtPropType);
        EditText FName = view.findViewById(R.id.edtPropPrice);
        EditText CNIC = view.findViewById(R.id.edtPropContact);
        EditText PhoneNo = view.findViewById(R.id.edtPropPurpose);
        EditText DOB = view.findViewById(R.id.edtProfDOB);
        EditText Gender = view.findViewById(R.id.edtProfGender);
        EditText Address = view.findViewById(R.id.edtProfAddress);

        Button update = view.findViewById(R.id.btnUpdate);

        DatabaseReference databaseReference=firebaseDatabase.getReference()
                .child("User Profile Info").child(Objects.requireNonNull(firebaseAuth.getUid()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfoModel model=snapshot.getValue(UserInfoModel.class);

                //  Glide.with(context).load(model.getImagePath()).into(profileImage);
                if(snapshot.exists()) {
                    assert model != null;
                   Name.setText(model.getProfileName());
                   Email.setText(model.getProfileEmail());
                    FName.setText(model.getProfileFatherName());
                    PhoneNo.setText(model.getProfilePhoneNo());
                    CNIC.setText(model.getProfileCnic());
                    DOB.setText(model.getProfileDOB());
                    Gender.setText(model.getProfileGender());
                    Address.setText(model.getProfileAddress());
                }else {
                    Toast.makeText(UserProfile.this, "No Record Available.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        dialogPlus.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("profileName", Name.getText().toString());
                map.put("profileEmail", Email.getText().toString());
                map.put("profileFatherName", FName.getText().toString());
                map.put("profilePhoneNo", PhoneNo.getText().toString());
                map.put("profileCnic", CNIC.getText().toString());
                map.put("profileDOB" ,DOB.getText().toString());
                map.put("profileGender",Gender.getText().toString());
                map.put("profileAddress",Address.getText().toString());
                map.put("UID",currentUserId);

                databaseReference.setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(UserProfile.this, "Profile Is Updated Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserProfile.this, UserProfile.class));
                                    dialogPlus.dismiss();
                                    finish();
                                }
                            }
                        });
    }});

    }

    private void OpenMyFavorites()
    {
       Intent favIntent=new Intent(UserProfile.this,MyFavorite.class);
       startActivity(favIntent);
       finish();
    }

    private void OpenMyProperties()
    {
        startActivity(new Intent(UserProfile.this,MyProperties.class));
        finish();
    }

    private void ContactUsMethod() {
        startActivity(new Intent(UserProfile.this,ContactUs.class));
        finish();
    }

    private void InviteFriendsMethod() {
        Intent it = new Intent();
        it.setAction(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT,"Hello,I would like to invite you to khitta.com.");
        it.setType("text/plain");
        startActivity(Intent.createChooser(it,"Choose App"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserProfile.this,MainActivity.class));
        finish();
    }
}