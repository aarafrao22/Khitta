package com.example.khitta.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khitta.Models.FavoritesModel;
import com.example.khitta.Models.PostsModel;
import com.example.khitta.PostDetails;
import com.example.khitta.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FavoritesAdapter extends FirebaseRecyclerAdapter<PostsModel,FavoritesAdapter.viewHolder> {

    Context context;

    public FavoritesAdapter(@NonNull FirebaseRecyclerOptions<PostsModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder,final int position, @NonNull PostsModel model) {
        Glide.with(holder.FavimageView.getContext()).load(model.getPath()).into(holder.FavimageView);
        holder.FavEdtRooms.setText(model.getProperty_bedrooms());
        holder.FavEdtBathrooms.setText(model.getProperty_washrooms());
        holder.f1.setText(model.getProperty_area());
        holder.f2.setText(model.getProperty_areaSize());
        holder.f3.setText(model.getProperty_purpose());
        holder.f4.setText(model.getProperty_title());
        holder.f5.setText(model.getProperty_location());
        holder.f6.setText(model.getProperty_description());
        holder.f7.setText(model.getProperty_purpose());
        holder.f8.setText(model.getProperty_uName());
        holder.f9.setText(model.getProperty_uEmail());
        holder.f10.setText(model.getProperty_uPhoneNo());
        holder.FavEdtPrice.setText("PKR :"+model.getProperty_price());

        holder.FavbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.FavimageView.getContext());
                builder.setTitle("Remove Panel");
                builder.setMessage("Are you sure to remove.?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        String id=user.getUid();
                        FirebaseDatabase.getInstance().getReference().child("Favorites Data")
                                .child(id)
                                .child(getRef(position).getKey())
                                .removeValue();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        // TODO: 12/21/2021
//        holder.FavcardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(context,PostDetails.class);
//
//                intent.putExtra("postImage" ,model.getPath());
//                intent.putExtra("postPrice" ,model.getProperty_price());
//                intent.putExtra("postLocation" ,model.getProperty_location());
//                intent.putExtra("postArea" ,model.getProperty_area());
//                intent.putExtra("postBeds" ,model.getProperty_bedrooms());
//                intent.putExtra("postBaths" ,model.getProperty_washrooms());
//                intent.putExtra("postPropertyType", model.getProperty_type());
//                intent.putExtra("postArea" ,model.getProperty_area());
//                intent.putExtra("postPurpose" ,model.getProperty_purpose());
//                intent.putExtra("postDescription" ,model.getProperty_description());
//                intent.putExtra("postUserName" ,model.getProperty_uName());
//                intent.putExtra("postPhoneNo",model.getProperty_uPhoneNo());
//                intent.putExtra("postEmail",model.getProperty_uEmail());
//                context.startActivity(intent);
//                ((Activity)context).finish();
//            }
//        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       viewHolder myView= new viewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_items_view,
                        parent,false));
                  return myView;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView FavimageView,FavbtnDelete;
        TextView FavEdtPrice,FavEdtRooms,FavEdtBathrooms,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10;
        CardView FavcardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            FavimageView=itemView.findViewById(R.id.FavviewImage);
            FavbtnDelete=itemView.findViewById(R.id.FavBtnDelete);
            FavEdtRooms=itemView.findViewById(R.id.FavEdtRooms);
            FavEdtBathrooms=itemView.findViewById(R.id.FavEdtBathrooms);
            f1=itemView.findViewById(R.id.f1);
            f2=itemView.findViewById(R.id.f2);
            f3=itemView.findViewById(R.id.f3);
            f4=itemView.findViewById(R.id.f4);
            f5=itemView.findViewById(R.id.f5);
            f6=itemView.findViewById(R.id.f6);
            f7=itemView.findViewById(R.id.f7);
            f8=itemView.findViewById(R.id.f8);
            f9=itemView.findViewById(R.id.f9);
            f10=itemView.findViewById(R.id.f10);
            FavEdtPrice=itemView.findViewById(R.id.FavEdtPrice);
            FavcardView=itemView.findViewById(R.id.Fav_card_view);
        }
    }
}
