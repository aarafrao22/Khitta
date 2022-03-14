package com.example.khitta.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khitta.Models.MyPropModel;
import com.example.khitta.Models.PostsModel;
import com.example.khitta.MyProperties;
import com.example.khitta.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyPropertiesAdapter extends FirebaseRecyclerAdapter<MyPropModel,MyPropertiesAdapter.ViewHolder>
{

    public MyPropertiesAdapter(@NonNull FirebaseRecyclerOptions<MyPropModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull MyPropModel model) {
        Glide.with(holder.imageView.getContext()).load(model.getPath()).into(holder.imageView);
        holder.tvViewLocation.setText(model.getProperty_location());
        holder.tvViewPurpose.setText(model.getProperty_purpose());
        holder.tvViewPrice.setText("PKR :" + model.getProperty_price());
        holder.tvDate.setText(model.getDate());

        //==button edit clickListener code start from here==//
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1000)
                        .create();

                View view = dialogPlus.getHolderView();
                EditText ownerName = view.findViewById(R.id.edtName);
                EditText propType = view.findViewById(R.id.edtPropType);
                EditText propPrice = view.findViewById(R.id.edtPropPrice);
                EditText propContact = view.findViewById(R.id.edtPropContact);
                EditText propPurpose = view.findViewById(R.id.edtPropPurpose);


                Button update = view.findViewById(R.id.btnUpdate);

                ownerName.setText(model.getProperty_uName());
                propType.setText(model.getProperty_type());
                propPrice.setText(model.getProperty_price());
                propContact.setText(model.getProperty_uPhoneNo());
                propPurpose.setText(model.getProperty_purpose());

                dialogPlus.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("property_uName", ownerName.getText().toString());
                        map.put("property_type", propType.getText().toString());
                        map.put("property_purpose", propPurpose.getText().toString());
                        map.put("property_price", propPrice.getText().toString());
                        map.put("property_uPhoneNo", propContact.getText().toString());

                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        String id=user.getUid();
                        FirebaseDatabase.getInstance().getReference().child("All Property Posts")
                                .child("Posts").
                                child(id).child(getRef(position).getKey())
                                .updateChildren(map)

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });

            }
        });

        //==button edit clickListener code END here==//

        //==button delete clickListener code start from here==//
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.imageView.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Are you sure to delete.?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        String id=user.getUid();
                        FirebaseDatabase.getInstance().getReference().child("All Property Posts")
                                .child("Posts").child(id)
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
        //==button delete clickListener code END here==//
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder myView= new ViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.my_properties_view,
                        parent,false));
        return myView;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView,btnEdit,btnDelete;
        TextView tvViewLocation,tvViewPurpose,tvViewPrice,tvDate;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView=itemView.findViewById(R.id.viewImage);
            btnEdit=itemView.findViewById(R.id.viewEdit);
            btnDelete=itemView.findViewById(R.id.viewDelete);
            tvViewLocation=itemView.findViewById(R.id.viewAddress);
            tvViewPurpose=itemView.findViewById(R.id.viewPurpose);
            tvViewPrice=itemView.findViewById(R.id.viewPrice);
            tvDate=itemView.findViewById(R.id.viewDate);
        }
    }
}
