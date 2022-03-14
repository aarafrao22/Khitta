package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddPost extends Activity {

    Spinner spPropertyType,spPurpose,spAreaSize;
    EditText pTitle,pDescription,pBedrooms,pWashrooms,pArea
            ,pPrice,contactName,contactEmail,contactPhone;
    private ImageView mImage;
    ImageButton backArrow;
    TextView mAddressPicker;
    private Bundle getBundle;

    String imagePath,imagePath2,imagePath3,StringpTitle,StringpDescription,StringpBedrooms,StringpWashrooms,StringpArea
            ,StringpPrice,StringcontactName,StringcontactEmail,StringcontactPhone,
            StringspPropertyType,StringspPurpose,StringspAreaSize,StringTvAddress,postDate;

    String StringAddressPicker ="test";
    String stringiName,stringiPhoneNo,stringiEmail;

    // =======firebase objects=====
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    //for multi images
    Button select;
    ImageView previous, next;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView total;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        init();
        PropertyImagesMethod();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //===getting Map address in intent==//
        getBundle=getIntent().getExtras();
        if (getBundle!=null)
        {
            StringAddressPicker = getBundle.getString("MapAddress");

            mAddressPicker.setText(StringAddressPicker);

        }
        //===END of getting address===///

        //==getting user name,email and phone number in post activity edit text==//
        FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
         databaseReference .child("User Profile Info").child(Objects.requireNonNull(mAuth.getUid()))
                 .addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         UserInfoModel model=snapshot.getValue(UserInfoModel.class);
                         if (snapshot.exists()){
                             assert model != null;
                             contactName.setText(model.getProfileName());
                             contactEmail.setText(model.getProfileEmail());
                             contactPhone.setText(model.getProfilePhoneNo());
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                         Toast.makeText(AddPost.this, ""+error, Toast.LENGTH_SHORT).show();
                     }
                 });
        }

        // Spinner Drop down elements for property type
        List<String> Property = new ArrayList<String>();
        Property.add("House");
        Property.add("Flat");
        Property.add("Upper Portion");
        Property.add("Lower Portion");
        Property.add("Farm House");
        Property.add("Room");
        Property.add("Residential Plot");
        Property.add("Commercial Plot");
        Property.add("Agriculture Land");
        Property.add("Industrial Land");
        Property.add("Plot File");
        Property.add("Plot Form");
        Property.add("Office");
        Property.add("Shop");
        Property.add("Warehouse");
        Property.add("Factory");
        Property.add("Building");
        Property.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> propertyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Property);

        // Drop down layout style - list view with radio button
        propertyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spPropertyType.setAdapter(propertyAdapter);


        // Spinner Drop down elements for purpose
        List<String> Purpose = new ArrayList<String>();
        Purpose.add("Sell");
        Purpose.add("Rent");
        Purpose.add("Want to buy");
        Purpose.add("Want to rent");

        // Creating adapter for spinner
        ArrayAdapter<String> purposeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Purpose);

        // Drop down layout style - list view with radio button
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spPurpose.setAdapter(purposeAdapter);

        // Spinner Drop down elements for Area
        List<String> Area = new ArrayList<String>();
        Area.add("Sq.Ft.");
        Area.add("Sq.M.");
        Area.add("Sq.Yd");
        Area.add("Marla");
        Area.add("Kanal");

        // Creating adapter for spinner
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Area);

        // Drop down layout style - list view with radio button
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spAreaSize.setAdapter(areaAdapter);



    }

    private void init()
    {
        spPropertyType=findViewById(R.id.spProperty);
        spPurpose=findViewById(R.id.spPurpose);
        spAreaSize=findViewById(R.id.spArea);
       // mImage=findViewById(R.id.propertyImage);
        pTitle=findViewById(R.id.propertyTitle);
        pDescription=findViewById(R.id.propertyDescription);
        pBedrooms=findViewById(R.id.propertyBedrooms);
        pWashrooms=findViewById(R.id.propertyWashrooms);
        pArea=findViewById(R.id.propertyArea);
        pPrice=findViewById(R.id.propertyPrice);
        contactName=findViewById(R.id.contactName);
        contactEmail=findViewById(R.id.contactEmail);
        contactPhone=findViewById(R.id.contactPhoneNo);
        backArrow=findViewById(R.id.backArrow);
        mAddressPicker=findViewById(R.id.tvAddress);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backarrow=new Intent(AddPost.this,MainActivity.class);
                startActivity(backarrow);
                finish();
            }
        });

        mAddressPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backarrow=new Intent(AddPost.this,MapsActivity.class);
                startActivity(backarrow);
                finish();
            }
        });
    }

     //=======Upload post to firebase database code===========
    public void UploadPost(View view)
    {
        StringTvAddress=mAddressPicker.getText().toString();
        StringpTitle=pTitle.getText().toString().trim();
        StringpDescription=pDescription.getText().toString().trim();
        StringpBedrooms=pBedrooms.getText().toString().trim();
        StringpWashrooms=pWashrooms.getText().toString().trim();
        StringpArea=pArea.getText().toString().trim();
        StringspAreaSize=spAreaSize.getSelectedItem().toString();
        StringpPrice=pPrice.getText().toString().trim();
        StringcontactName=contactName.getText().toString().trim();
        StringcontactEmail=contactEmail.getText().toString().trim();
        StringcontactPhone=contactPhone.getText().toString().trim();
        StringspPropertyType=spPropertyType.getSelectedItem().toString();
        StringspPurpose=spPurpose.getSelectedItem().toString();
        postDate =new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        //====validation here=========

        if (StringTvAddress.equals("")){
            mAddressPicker.setText("Please mark your location on map");
        }
        else if (StringpTitle.equals(""))
        {
            pTitle.setError("Please fill this field.");
        }
        else if (StringpBedrooms.equals(""))
        {
            pBedrooms.setError("Please fill this field.");
        }
        else if (StringpWashrooms.equals(""))
        {
            pWashrooms.setError("Please fill this field.");
        }
        else if (StringpArea.equals(""))
        {
            pArea.setError("Please fill this field.");
        }
        else if (StringpPrice.equals(""))
        {
            pPrice.setError("Please fill this field.");
        }
        else if (StringcontactName.equals(""))
        {
            contactName.setError("Please fill this field.");
        }
        else if (StringcontactEmail.equals(""))
        {
            contactEmail.setError("Please fill this field.");
        }
        else if (StringcontactPhone.equals(""))
        {
            contactPhone.setError("Please fill this field.");
        }
        else
            {
                String currentUserId = mAuth.getCurrentUser().getUid();

                HashMap<String,String> PostData = new HashMap<String,String>();

                PostData.put("path",imagePath);
                PostData.put("path2",imagePath2);
                PostData.put("path3",imagePath3);
                PostData.put("property_location",StringTvAddress);
                PostData.put("property_type",StringspPropertyType);
                PostData.put("property_purpose",StringspPurpose);
                PostData.put("property_title",StringpTitle);
                PostData.put("property_price",StringpPrice);
                PostData.put("property_description",StringpDescription);
                PostData.put("property_bedrooms",StringpBedrooms);
                PostData.put("property_bedrooms",StringpWashrooms);
                PostData.put("property_area",StringpArea);
                PostData.put("property_areaSize",StringspAreaSize);
                PostData.put("property_uName",StringcontactName);
                PostData.put("property_uEmail",StringcontactEmail);
                PostData.put("property_uPhoneNo",StringcontactPhone);
                PostData.put("date",postDate);
                PostData.put("userID",currentUserId);

                //========= store in firebase============

                databaseReference.child("All Property Posts").child("Posts")
                        .child(currentUserId)
                        .child(String.valueOf(System.currentTimeMillis()))
                        .setValue(PostData)
                        .addOnCompleteListener(AddPost.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(AddPost.this, "Your post is Added Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddPost.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
        }
    }

    //==select multiple property images code start from here==//
    private void PropertyImagesMethod() {

        // for multi images

        select = findViewById(R.id.select);
        total = findViewById(R.id.text);
        imageView = findViewById(R.id.image);
        previous = findViewById(R.id.previous);
        mArrayUri = new ArrayList<Uri>();

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });
        next = findViewById(R.id.next);

        // click here to select next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(AddPost.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click here to view previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                }
            }
        });

        imageView = findViewById(R.id.image);

        // click here to select image
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);

                    //upload to firebase
                    mStorageRef.child("images/").child(System.currentTimeMillis() + ".jpg").putFile(imageurl)
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
                                                    imagePath2 = uri.toString();
                                                    imagePath3 = uri.toString();
                                                    Toast.makeText(AddPost.this, "Path selected", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }}});
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;



            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }
    //==select multiple property images code END here==//

    //=========backpress button=========
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddPost.this,MainActivity.class));
        finish();
    }

}