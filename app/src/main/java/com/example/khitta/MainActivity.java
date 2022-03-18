package com.example.khitta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khitta.Adapters.PostsAdapter;
import com.example.khitta.Models.PostsModel;
import com.example.khitta.Models.UserInfoModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    Context context;

    LinearLayout searchBar;

     FirebaseAuth mAuth;
//    FirebaseUser firebaseUser=mAuth.getCurrentUser();
    RecyclerView recyclerView;
    private DatabaseReference myRef;
    FirebaseDatabase firebaseDatabase;
    private ArrayList<PostsModel> myPostsList;
    private PostsAdapter myPostsAapter;

    ImageButton btnDrawer,btnSearch;
    //searchbar items
    TextView tvSearch;
    boolean[] selectedItems;
    ArrayList<Integer> dayList=new ArrayList<>();
    String [] dayArray ={"Bed Rooms", "Wash Rooms", "Location", "Price"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        init();
        GetDataFromFirebase();
        toolbar=findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        mNavigationView=findViewById(R.id.navigation_view);
        //setSupportActionBar(toolbar);


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



     mNavigationView.setNavigationItemSelectedListener(Listener);

     //==set nav menu item LogOut text to LogIn Code==//
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//        if (user ==null)
//        {
//        Menu menu=mNavigationView.getMenu();
//        MenuItem nav_login=menu.findItem(R.id.navLogOut);
//        nav_login.setTitle("LogIn");
//        }
    }

    private void init()
    {
//        bottomHome=findViewById(R.id.bottomHome);
//        bottomAdd=findViewById(R.id.bottomAdd);
//        bottomFav=findViewById(R.id.bottomFavorites);
//        bottomProfile=findViewById(R.id.bottomProfile);
        btnDrawer=findViewById(R.id.menuIcon);
        searchBar=findViewById(R.id.searchBar);
        tvSearch=findViewById(R.id.tvSearch);
        btnSearch=findViewById(R.id.btnSearch);

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
//        if (user !=null) {
//            bottomAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intentAdd=new Intent(MainActivity.this,AddPost.class);
//                    startActivity(intentAdd);
//                    finish();
//                }
//            });
//        }else{
//            //==========bottom add icon click condition code end here==========
//        }
//
//
//        bottomProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//                if (user !=null)
//                {
//                    Intent intentProfile = new Intent(MainActivity.this, UserProfile.class);
//                    startActivity(intentProfile);
//                    finish();
//                }
//                else
//                {
//                    Intent intentprofile=new Intent(MainActivity.this,SignUp.class);
//                    startActivity(intentprofile);
//                    finish();
//                }
//            }
//        });
//
//        bottomFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,MyFavorite.class));
//                finish();
//            }
//        });
//            bottomAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intentAdd=new Intent(MainActivity.this,SignUp.class);
//                    startActivity(intentAdd);
//                    finish();
//                }
//            });
        
        //==search bar coding==//
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBarCoding();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBarCoding();
            }
        });

    }

    //==Search bar coding method start from here==//
    private void SearchBarCoding() {


        final DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.search_popup))
                .setGravity(Gravity.CENTER)
                .setExpanded(true, 1300)
                .setCancelable(true)
                .create();


        View view = dialogPlus.getHolderView();
        Spinner s1 = view.findViewById(R.id.s1);
        Spinner s2 = view.findViewById(R.id.s2);
        Spinner s3 = view.findViewById(R.id.s3);
        Spinner s4 = view.findViewById(R.id.s4);
        Spinner s5 = view.findViewById(R.id.s5);

        Button Popup_search = view.findViewById(R.id.btnSearch);
        
        String currentUserId = mAuth.getCurrentUser().getUid();
      DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference();
      myRef1.child("All Property Posts").child("Posts")
                .child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> Bedrooms = new ArrayList<String>();
                final List<String> SqFeetList = new ArrayList<String>();
                final List<String> PriceList = new ArrayList<String>();
                final List<String> PropTypeList = new ArrayList<String>();
                final List<String> LocationList = new ArrayList<String>();

                for (DataSnapshot mySnapshot: dataSnapshot.getChildren()) {
                    String propBedrooms = mySnapshot.child("property_bedrooms").getValue(String.class);
                    Bedrooms.add(propBedrooms);
                   //2nd list
                    String sqfeet=mySnapshot.child("property_area").getValue(String.class);
                    SqFeetList.add(sqfeet);
                    //3rd list
                    String Price=mySnapshot.child("property_price").getValue(String.class);
                    PriceList.add(Price);

                    //4th list
                    String type=mySnapshot.child("property_type").getValue(String.class);
                    PropTypeList.add(type);

                    //5th list
                    String loc=mySnapshot.child("property_location").getValue(String.class);
                    LocationList.add(loc);
                }

                ArrayAdapter<String> bedroomsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, Bedrooms);
                bedroomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s1.setAdapter(bedroomsAdapter);

                ArrayAdapter<String> sqFeetAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, SqFeetList);
                sqFeetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s2.setAdapter(sqFeetAdapter);

                //3rd Adapter for price
                ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, PriceList);
                priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s3.setAdapter(priceAdapter);

                //4th Adapter for price
                ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, PropTypeList);
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s4.setAdapter(typeAdapter);

                //5th Adapter for price
                ArrayAdapter<String> locAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, LocationList);
                locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s5.setAdapter(locAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialogPlus.show();
        //yaha masla hy//=======
//        Popup_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: 1/23/2022
//            }
//        });

    }
    //==Search bar coding method END here==//

    //========navigation item click listener coding from here============//
    private NavigationView.OnNavigationItemSelectedListener Listener=new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mNavigationView.getMenu().findItem(R.id.navLogOut).setVisible(true);
            int id=item.getItemId();

            if (id==R.id.navProfile)
            {
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null)
                {
                    startActivity(new Intent(MainActivity.this, UserProfile.class));
                    finish();
                }else {
                    startActivity(new Intent(MainActivity.this, SignUp.class));
                    finish();
                }

            }
            else if (id==R.id.navAdd)
            {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null)
                {
                startActivity(new Intent(MainActivity.this,AddPost.class));
                finish();
                }else {
                    Intent intentAdd=new Intent(MainActivity.this,SignUp.class);
                    startActivity(intentAdd);
                    finish();
                }

            }
            else if (id==R.id.navFavorites)
            {
                startActivity(new Intent(MainActivity.this,MyFavorite.class));
                finish();

            }
            else if (id==R.id.navMyProperty)
            {
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null)
                {
                    startActivity(new Intent(MainActivity.this, MyProperties.class));
                    finish();
                }else {
                    startActivity(new Intent(MainActivity.this, SignUp.class));
                    finish();
                }

            }
            else if (id==R.id.navAdmin){
                startActivity(new Intent(MainActivity.this, AdminHome.class));
                finish();
            }
            else if (id==R.id.navLogOut)
            {
               FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                    Toast.makeText(MainActivity.this,  "Successfully Sign out!", Toast.LENGTH_SHORT).show();

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
                        myPostsAapter = new PostsAdapter(myPostsList, MainActivity.this);
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
        new AlertDialog.Builder(MainActivity.this)
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