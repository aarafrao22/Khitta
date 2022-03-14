package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class PostDetails extends Activity
{
    private static final int REQUEST_CALL=1;
    private static final int REQUEST_SMS=100;
     ImageView imageView;
     TextView tvPrice,tvAddress,tvArea,tvBeds,tvBaths,tvUserName,tvPropertyType,
              tvPrice2,tvBeds2,tvBaths2,tvArea2,tvPurpose,tvAddress2,tvDescription,
               tvCall,tvEmail,tvSms;
     Button btnEmail,btnCall,btnSms;
    ImageButton backArrow;

     //Strings of text views
    String DetailsImage,tvDetailsPrice,tvDetailsAddress,tvDetailsArea,tvDetailsBeds,tvDetailsBaths,
             tvDetailsUserName,tvDetailsPropType,tvDetailsPrice2,tvDetailsBeds2,tvDetailsBaths2,
             tvDetailsArea2,tvDetailsPurpose,tvDetailsAddress2,tvDetailsDescription,
             tvDetailsPhoneNo,tvDetailsEmail;

    Bundle myCardViewBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        inti();
        GetpostToDetailsActivity();
    }

    private void GetpostToDetailsActivity()
    {

        myCardViewBundle=getIntent().getExtras();

        DetailsImage=myCardViewBundle.getString("postImage");
        tvDetailsPrice=myCardViewBundle.getString("postPrice");
        tvDetailsAddress=myCardViewBundle.getString("postLocation");
        tvDetailsArea=myCardViewBundle.getString("postArea");
        tvDetailsBeds=myCardViewBundle.getString("postBeds");
        tvDetailsBaths=myCardViewBundle.getString("postBaths");
        tvDetailsPropType=myCardViewBundle.getString("postPropertyType");
        tvDetailsUserName=myCardViewBundle.getString("postUserName");
        tvDetailsPrice2=myCardViewBundle.getString("postPrice");
        tvDetailsBeds2=myCardViewBundle.getString("postBeds");
        tvDetailsBaths2=myCardViewBundle.getString("postBaths");
        tvDetailsArea2=myCardViewBundle.getString("postArea");
        tvDetailsPurpose=myCardViewBundle.getString("postPurpose");
        tvDetailsAddress2=myCardViewBundle.getString("postLocation");
        tvDetailsDescription=myCardViewBundle.getString("postDescription");
        tvDetailsPhoneNo=myCardViewBundle.getString("postPhoneNo");
        tvDetailsEmail=myCardViewBundle.getString("postEmail");

        //==set card view data to post details activity===//

        Glide.with(PostDetails.this).load(DetailsImage).into(imageView);
        tvPrice.setText("PKR: " + tvDetailsPrice);
        tvAddress.setText(tvDetailsAddress);
        tvArea.setText(tvDetailsArea);
        tvBeds.setText(tvDetailsBeds);
        tvBaths.setText(tvDetailsBaths);
        tvUserName.setText(tvDetailsUserName);
        tvPropertyType.setText(tvDetailsPropType);
        tvPrice2.setText("PKR: " +tvDetailsPrice2);
        tvBeds2.setText(tvDetailsBeds2);
        tvBaths2.setText(tvDetailsBaths2);
        tvArea2.setText(tvDetailsArea2);
        tvPurpose.setText(tvDetailsPurpose);
        tvAddress2.setText(tvDetailsAddress2);
        tvDescription.setText(tvDetailsDescription);
        tvCall.setText(tvDetailsPhoneNo);
        tvSms.setText(tvDetailsPhoneNo);
        tvEmail.setText(tvDetailsEmail);

        //======End of post details======//
    }

    private void inti() {
        imageView=findViewById(R.id.postDetailImage);
        tvPrice=findViewById(R.id.postDetailsPrice);
        tvAddress=findViewById(R.id.postDetailsAddress);
        tvArea=findViewById(R.id.postDetailsArea);
        tvBeds=findViewById(R.id.postDetailsBeds);
        tvBaths=findViewById(R.id.postDetailsBaths);
        tvUserName=findViewById(R.id.postDetailsUserName);
        tvPropertyType=findViewById(R.id.postDetailsType);
        tvPrice2=findViewById(R.id.postDetailsPrice2);
        tvBeds2=findViewById(R.id.postDetailsBeds2);
        tvBaths2=findViewById(R.id.postDetailsBaths2);
        tvArea2=findViewById(R.id.postDetailsArea2);
        tvPurpose=findViewById(R.id.postDetailsPurpose);
        tvAddress2=findViewById(R.id.postDetailsAddress2);
        tvDescription=findViewById(R.id.postDetailsDescription);
        btnEmail=findViewById(R.id.btnEmail);
        btnCall=findViewById(R.id.btnCall);
        btnSms=findViewById(R.id.btnSms);
        tvCall=findViewById(R.id.tvCall);
        tvEmail=findViewById(R.id.tvEmail);
        tvSms=findViewById(R.id.tvSms);
        backArrow=findViewById(R.id.backArrow);

        //==back press on arrow in tolbar
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backarrow=new Intent(PostDetails.this,MainActivity.class);
                startActivity(backarrow);
                finish();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallButton();
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSButton();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailButton();
            }
        });
    }

    //==when user click on Email button these lines of codes will be run==//
    private void EmailButton() {

        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, new String[]{tvEmail.getText().toString()});
        it.putExtra(Intent.EXTRA_SUBJECT,"hi");
        it.putExtra(Intent.EXTRA_TEXT,"Hello,I would "+
                "                      \"like to inquire about your property.Please contact" +
                "                      \" me at your earliest convenience");
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it,"Choose Mail App"));

    }

    //==when user click on SMS button this code will be run===//
    private void SMSButton()
    {
        String number=tvCall.getText().toString();
        if (number.trim().length()>0){
            if (ContextCompat.checkSelfPermission(PostDetails.this, Manifest.permission
                    .SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(PostDetails.this,new String[]
                        {Manifest.permission.SEND_SMS},REQUEST_SMS);
            }else {
               // String dial="Hello world:"+number;
              Uri uri=Uri.parse("smsto:" +number);
              Intent intent=new Intent(Intent.ACTION_SENDTO,uri);
              intent.putExtra("sms_body","Hello,I would " +
                      "like to inquire about your property.Please contact" +
                      " me at your earliest convenience.");
              startActivity(intent);
            }
        }
    }

    //==when user click on CALL button this code will be run===//
    private void CallButton()
    {
        String number=tvCall.getText().toString();
        if (number.trim().length()>0){
            if (ContextCompat.checkSelfPermission(PostDetails.this, Manifest.permission
            .CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(PostDetails.this,new String[]
                        {Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else {
                String dial="tel:"+number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                CallButton();
            }
            else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(PostDetails.this,MainActivity.class));
        super.onBackPressed();
    }
}