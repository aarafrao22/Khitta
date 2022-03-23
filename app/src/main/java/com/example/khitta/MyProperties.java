package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.khitta.Adapters.MyPropertiesAdapter;
import com.example.khitta.Adapters.PostsAdapter;
import com.example.khitta.Models.MyPropModel;
import com.example.khitta.Models.PostsModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyProperties extends Activity{
    RecyclerView MyPropRecycler;
    ArrayList<PostsModel> myPropList;
    MyPropertiesAdapter myPropertiesAdapter;
    DatabaseReference dbReference;
    FirebaseAuth firebaseAuth;
    ImageButton backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);
        backArrow=findViewById(R.id.backArrow);
        //==back press on arrow in tolbar
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backarrow=new Intent(MyProperties.this,MainActivity.class);
                startActivity(backarrow);
                finish();
            }
        });

        MyPropRecycler=findViewById(R.id.MyPropertiesRv);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        MyPropRecycler.setLayoutManager(layoutManager);
        //init();
       // GetCurrentUserProperty();
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user !=null){
            FirebaseRecyclerOptions<MyPropModel> options= new FirebaseRecyclerOptions.Builder<MyPropModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("All Property Posts").child("Posts")
            .child(user.getUid()),MyPropModel.class).build();

        myPropertiesAdapter=new MyPropertiesAdapter(options);
        MyPropRecycler.setAdapter(myPropertiesAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myPropertiesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myPropertiesAdapter.stopListening();
    }

    //    private void GetCurrentUserProperty() {
//        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null)
//        {
//            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
//            .child("All Property Posts").child("Posts")
//            .child(Objects.requireNonNull(user.getUid()));
//
//            databaseReference.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        PostsModel model = snapshot.getValue(PostsModel.class);
//                        myPropList.add(model);
//                        myPropertiesAdapter = new MyPropertiesAdapter(myPropList, MyProperties.this);
//                        myPropertiesAdapter.notifyDataSetChanged();
//                        MyPropRecycler.setAdapter(myPropertiesAdapter);
//
//            }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error)
//                {
//                }
//                });
//        }else {
//            Toast.makeText(MyProperties.this, "No Record Found!!", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyProperties.this,MainActivity.class));
        finish();
    }
}