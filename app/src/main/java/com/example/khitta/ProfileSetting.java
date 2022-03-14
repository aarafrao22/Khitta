package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khitta.Models.UserInfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileSetting extends Activity {

    ImageView profileImage;
    EditText profileSettingAddress,profileSettingGender,profileSettingPhoneNo
            ,profileSettingEmail,profileSettingName,profileSettingFName,profileSettingCnic,
            profileSettingDOB;
    Button btnSettingUpdate;

     String pSettingName, pSettingEmail, pSettingGender, pSettingPhoneno
            ,pSettingAddress,imagePath,pSettingFName,pSettingCnic,pSettingDOB;

    // =======firebase objects=====
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        init();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    public void init()
    {
        profileImage=findViewById(R.id.profileImage);
        profileSettingAddress=findViewById(R.id.profileSettingAddress);
        profileSettingGender=findViewById(R.id.profileSettingGender);
        profileSettingPhoneNo=findViewById(R.id.profileSettingPhoneNo);
        profileSettingEmail=findViewById(R.id.profileSettingEmail);
        profileSettingName=findViewById(R.id.profileSettingName);
        btnSettingUpdate=findViewById(R.id.btnSettingUpdate);
        profileSettingFName=findViewById(R.id.profileSettingFName);
        profileSettingCnic=findViewById(R.id.profileSettingCnic);
        profileSettingDOB=findViewById(R.id.profileSettingDOB);

        btnSettingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });
    }

    private void UpdateProfile()
    {
        pSettingName=profileSettingName.getText().toString().trim();
        pSettingEmail=profileSettingEmail.getText().toString().trim();
        pSettingPhoneno=profileSettingPhoneNo.getText().toString().trim();
        pSettingGender=profileSettingGender.getText().toString().trim();
        pSettingAddress=profileSettingAddress.getText().toString().trim();
        pSettingFName=profileSettingFName.getText().toString().trim();
        pSettingCnic=profileSettingCnic.getText().toString().trim();
        pSettingDOB=profileSettingDOB.getText().toString().trim();

        if (pSettingName.equals(""))
        {
            profileSettingName.setError("Please fill this field!!");
        }
        else if (pSettingPhoneno.equals(""))
        {
            profileSettingPhoneNo.setError("Please fill this field!!");
        }
        else if (pSettingGender.equals(""))
        {
            profileSettingGender.setError("Please fill this field!!");
        }
        else if (pSettingAddress.equals(""))
        {
            profileSettingAddress.setError("Please fill this field!!");
        }
        else
            {
                String currentUserId = mAuth.getCurrentUser().getUid();

                HashMap<String,String> ProfileData = new HashMap<String,String>();
                ProfileData.put("UID",currentUserId);
                ProfileData.put("imagePath",imagePath);
                ProfileData.put("profileName",pSettingName);
                ProfileData.put("profileEmail",pSettingEmail);
                ProfileData.put("profilePhoneNo",pSettingPhoneno);
                ProfileData.put("profileGender",pSettingGender);
                ProfileData.put("profileAddress",pSettingAddress);
                ProfileData.put("profileFatherName",pSettingFName);
                ProfileData.put("profileCnic",pSettingCnic);
                ProfileData.put("profileDOB",pSettingDOB);

                databaseReference.child("User Profile Info")
               .child(currentUserId).setValue(ProfileData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ProfileSetting.this, "Profile Is Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileSetting.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
    }

    // =======open gallery code=====
    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,0);
    }

    //=======select image from gallery and upload to database coe===========
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    profileImage.setImageURI(selectedImage);

                    mStorageRef.child("images/").child("ProfileImages").child(System.currentTimeMillis()+".jpg").putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imagePath = uri.toString();
                                                    Toast.makeText(ProfileSetting.this, "Path selected", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            imagePath="no_image";
                                            Toast.makeText(ProfileSetting.this, "Path not selected", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    Toast.makeText(ProfileSetting.this, "image uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                break;
        }

    }

    //=========backpress button=========
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileSetting.this,UserProfile.class));
        finish();
    }
}