package com.example.khitta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.khitta.Adapters.FavoritesAdapter;
import com.example.khitta.Adapters.MyPropertiesAdapter;
import com.example.khitta.Models.FavoritesModel;
import com.example.khitta.Models.MyPropModel;
import com.example.khitta.Models.PostsModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class MyFavorite extends AppCompatActivity {
    RecyclerView MyFavRecycler;
    FavoritesAdapter myFavAdapter;
    DatabaseReference dbReference;
    FirebaseAuth firebaseAuth;
    ImageButton backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        backArrow=findViewById(R.id.backArrow);
        //==back press on arrow in tolbar
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backarrow=new Intent(MyFavorite.this,MainActivity.class);
                startActivity(backarrow);
                finish();
            }
        });

        MyFavRecycler=findViewById(R.id.favoriteRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        MyFavRecycler.setLayoutManager(layoutManager);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user !=null){
        FirebaseRecyclerOptions<PostsModel> options=
                new FirebaseRecyclerOptions.Builder<PostsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("Favorites Data")
                        .child(Objects.requireNonNull(user.getUid())),PostsModel.class)
                        .build();

        myFavAdapter=new FavoritesAdapter(options);
        MyFavRecycler.setAdapter(myFavAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user !=null) {
            myFavAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user !=null) {
            myFavAdapter.stopListening();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyFavorite.this,MainActivity.class));
        finish();
    }
}

