package com.example.khitta.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khitta.Models.PostsModel;
import com.example.khitta.PostDetails;
import com.example.khitta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    ArrayList<PostsModel> postsModelArrayList=new ArrayList<>();
    Context context;
    FirebaseAuth mAuth;
    Task<Void> myRef;
    private boolean isLiked;
    public static final String ACTION_LIKE_IMAGE_CLICK="action_like_image_click";

    public PostsAdapter(ArrayList<PostsModel> postsModelArrayList, Context context) {
        this.postsModelArrayList = postsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder myView= new ViewHolder
                (LayoutInflater.from(context).inflate(R.layout.my_item_view,
                parent,false));
        return myView;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        PostsModel postItems=postsModelArrayList.get(position);

        Glide.with(context).load(postItems.getPath()).into(holder.viewImage);
        holder.tvViewLocation.setText(postItems.getProperty_location());
        holder.tvViewPurpose.setText(postItems.getProperty_purpose());
        holder.tvViewPrice.setText("PKR :" + postItems.getProperty_price());
        holder.TvViewArea.setText(postItems.getProperty_area());
        holder.tvViewSqr.setText(postItems.getProperty_areaSize());
        holder.tvViewBeds.setText(postItems.getProperty_bedrooms());
        holder.tvViewBaths.setText(postItems.getProperty_washrooms());
        holder.tvViewDate.setText(postItems.getDate());
        holder.tvViewPropType.setText(postItems.getProperty_type());
        holder.TvViewPropTitle.setText(postItems.getProperty_title());
        holder.tvViewDesc.setText(postItems.getProperty_description());
        holder.tvViewUserEmail.setText(postItems.getProperty_uEmail());
        holder.tvViewUserName.setText(postItems.getProperty_uName());
        holder.tvViewUserPhone.setText(postItems.getProperty_uPhoneNo());
        holder.tvViewUserId.setText(postItems.getUserId());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PostDetails.class);

                intent.putExtra("postImage" ,postItems.getPath());
                intent.putExtra("postPrice" ,postItems.getProperty_price());
                intent.putExtra("postLocation" ,postItems.getProperty_location());
                intent.putExtra("postArea" ,postItems.getProperty_area());
                intent.putExtra("postBeds" ,postItems.getProperty_bedrooms());
                intent.putExtra("postBaths" ,postItems.getProperty_washrooms());
                intent.putExtra("postPropertyType", postItems.getProperty_type());
                intent.putExtra("postArea" ,postItems.getProperty_area());
                intent.putExtra("postPurpose" ,postItems.getProperty_purpose());
                intent.putExtra("postDescription" ,postItems.getProperty_description());
                intent.putExtra("postUserName" ,postItems.getProperty_uName());
                intent.putExtra("postPhoneNo",postItems.getProperty_uPhoneNo());
                intent.putExtra("postEmail",postItems.getProperty_uEmail());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey!! Check Out this new property at: http://Khitta.com");
                context.startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        //==Add to favorites button code start from here==//
        holder.FavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> favData = new HashMap<String, String>();
                favData.put("path",postItems.getPath() );
                favData.put("property_price", postItems.getProperty_price());
                favData.put("property_bedrooms", postItems.getProperty_bedrooms());
                favData.put("property_washrooms", postItems.getProperty_washrooms());
                favData.put("property_location", postItems.getProperty_location());
                favData.put("property_title",postItems.getProperty_title());
                favData.put("property_type",postItems.getProperty_type());
                favData.put("property_area",postItems.getProperty_area());
                favData.put("property_purpose",postItems.getProperty_purpose());
                favData.put("property_areaSize",postItems.getProperty_areaSize());
                favData.put("property_description",postItems.getProperty_description());
                favData.put("property_uName",postItems.getProperty_uName());
                favData.put("property_uEmail", postItems.getProperty_uEmail());
                favData.put("property_uPhoneNo",postItems.getProperty_uPhoneNo());
                String userId=FirebaseAuth.getInstance().getUid();
                favData.put("userId",userId);
                if (userId !=null){
                myRef= FirebaseDatabase.getInstance().getReference()
                        .child("Favorites Data").child(userId)
                        .child(String.valueOf(System.currentTimeMillis()))
                        .setValue(favData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    holder.FavIcon.setImageResource(R.drawable.fav_red);
                                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }else {
                    Toast.makeText(context, "login first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return postsModelArrayList.size();
    }

    //==static class of my view holder==//
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //initialize widgets
        CardView card_view;
        ImageView viewImage,shareIcon,FavIcon;
        TextView tvViewLocation,tvViewPurpose,tvViewPrice,TvViewArea,tvViewSqr,TvViewAreaSiza,TvViewPropTitle,
        tvViewBeds,tvViewBaths,tvViewDesc,tvViewPropType,tvViewUserEmail,
        tvViewUserName,tvViewUserPhone,tvViewUserId,tvViewDate;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        viewImage=itemView.findViewById(R.id.viewImage);
        tvViewLocation=itemView.findViewById(R.id.viewAddress);
        tvViewPrice=itemView.findViewById(R.id.viewPrice);
        tvViewPurpose=itemView.findViewById(R.id.viewPurpose);
        TvViewArea=itemView.findViewById(R.id.viewArea);
        tvViewSqr=itemView.findViewById(R.id.viewSqr);
        TvViewAreaSiza=itemView.findViewById(R.id.viewPropAreaSize);
        TvViewPropTitle=itemView.findViewById(R.id.viewPropTitle);
        tvViewBeds=itemView.findViewById(R.id.viewBeds);
        tvViewBaths=itemView.findViewById(R.id.viewBaths);
        tvViewDate=itemView.findViewById(R.id.viewDate);
        tvViewDesc=itemView.findViewById(R.id.viewDescription);
        tvViewPropType=itemView.findViewById(R.id.viewPropType);
        tvViewUserEmail=itemView.findViewById(R.id.viewUserEmail);
        tvViewUserName=itemView.findViewById(R.id.viewUserName);
        tvViewUserPhone=itemView.findViewById(R.id.viewUserPhone);
        tvViewUserId=itemView.findViewById(R.id.viewUserId);
        shareIcon=itemView.findViewById(R.id.Btnshare);
        FavIcon=itemView.findViewById(R.id.viewFav);
        card_view=itemView.findViewById(R.id.card_view);

    }
}
}
