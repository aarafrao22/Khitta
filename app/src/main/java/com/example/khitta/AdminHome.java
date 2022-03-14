package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.khitta.Adapters.PostsAdapter;
import com.example.khitta.Models.PostsModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    Context context;

    LinearLayout bottomHome,bottomAdd,bottomFav,bottomProfile;

    FirebaseAuth mAuth;
    //    FirebaseUser firebaseUser=mAuth.getCurrentUser();
    RecyclerView recyclerView;
    private DatabaseReference myRef;
    FirebaseDatabase firebaseDatabase;
    private ArrayList<PostsModel> myPostsList;
    private PostsAdapter myPostsAapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mAuth=FirebaseAuth.getInstance();
        init();
        GetDataFromFirebase();
        toolbar=findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        mNavigationView=findViewById(R.id.navigation_view);
        // setSupportActionBar(toolbar);


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(Listener);
    }

    private void init()
    {
        bottomHome=findViewById(R.id.bottomHome);
        bottomAdd=findViewById(R.id.bottomAdd);
        bottomFav=findViewById(R.id.bottomFavorites);
        bottomProfile=findViewById(R.id.bottomProfile);

        recyclerView=findViewById(R.id.post_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        //set recyclerview order in reverse to show latest post at the top
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.hasFixedSize();
        myPostsList=new ArrayList<>();




        //==========bottom add icon click condition code start from here==========
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null)
        {
            bottomAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentAdd=new Intent(AdminHome.this,AddPost.class);
                    startActivity(intentAdd);
                    finish();
                }
            });
        }else
        {
            bottomAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentAdd=new Intent(AdminHome.this,SignUp.class);
                    startActivity(intentAdd);
                    finish();
                }
            });
            //==========bottom add icon click condition code end here==========
        }


        bottomProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null)
                {
                    Intent intentProfile = new Intent(AdminHome.this, UserProfile.class);
                    startActivity(intentProfile);
                    finish();
                }
                else
                {
                    Intent intentprofile=new Intent(AdminHome.this,SignUp.class);
                    startActivity(intentprofile);
                    finish();
                }
            }
        });

        bottomFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHome.this,MyFavorite.class));
                finish();
            }
        });

    }

    //========navigation item click listener coding from here============//
    private NavigationView.OnNavigationItemSelectedListener Listener=new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();

            if (id==R.id.navProfile)
            {
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null)
                {
                    startActivity(new Intent(AdminHome.this, UserProfile.class));
                    finish();
                }else {
                    startActivity(new Intent(AdminHome.this, SignUp.class));
                    finish();
                }

            }
            else if (id==R.id.navAdd)
            {
                startActivity(new Intent(AdminHome.this,AddPost.class));
                finish();

            }
            else if (id==R.id.navFavorites)
            {
                startActivity(new Intent(AdminHome.this,MyFavorite.class));
                finish();

            }
            else if (id==R.id.navMyProperty)
            {
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null)
                {
                    startActivity(new Intent(AdminHome.this, MyProperties.class));
                    finish();
                }else {
                    startActivity(new Intent(AdminHome.this, SignUp.class));
                    finish();
                }

            }
            else if (id==R.id.navAdmin){
                startActivity(new Intent(AdminHome.this, AdminHome.class));
                finish();
            }
            else if (id==R.id.navLogOut)
            {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminHome.this,  "Successfully Sign out!", Toast.LENGTH_SHORT).show();
                item.setTitle("LogIn");

            }
            return true;
        }
    };

    //=========getting posts data from firebase database===
    private void GetDataFromFirebase()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                .getReference().child("All Property Posts")
                .child("Posts");

        //this line syced the app ofline
        databaseReference.keepSynced(true);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PostsModel model = ds.getValue(PostsModel.class);
                        // PostsModel model=new PostsModel();
                        myPostsList.add(model);
                        myPostsAapter = new PostsAdapter(myPostsList, AdminHome.this);
                        myPostsAapter.notifyDataSetChanged();
                        recyclerView.setAdapter(myPostsAapter);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//===============back press button code here===========//

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(AdminHome.this)
                .setTitle("Exit Application !")
                .setIcon(R.drawable.exit)
                .setMessage("Are you sure to Exit!")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }
}