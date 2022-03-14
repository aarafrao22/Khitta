package com.example.khitta;//package com.example.khitta;
//

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.khitta.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    Button fabDone;
    String finalAddress = "";
    private GoogleMap mMap;
    private Geocoder geo;
    private final double KARACHI_LAT = 24.860966;
    private final double KARACHI_LNG = 66.990501;
    private ActivityMapsBinding binding;
    boolean isPermissionGranted;
    LocationRequest locationRequest;

    SearchView searchView;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //searchView=findViewById(R.id.searchLocation);
        fabDone = findViewById(R.id.fabDone);
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickAddress();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geo = new Geocoder(this);

        checkPermission();

        //searchView coding start from here====

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //  mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        // on map open Add a marker in karachi and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng karachi = new LatLng(24.860966, 66.990501);
//        MarkerOptions markerOptions=new MarkerOptions()
//                .position(karachi)
//                .title("Karachi");
//        mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(karachi, 14);
        mMap.moveCamera(cameraUpdate);

        //=====add a marker on your location in map code start from here====//
        if (mMap != null) {
            geo = new Geocoder(this, Locale.getDefault());

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        if (geo == null)
                            geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address.size() > 0) {
                            String fullAddress = address.get(0).getAddressLine(0);
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(fullAddress)
                                    .draggable(true)
                            );
                            finalAddress = fullAddress;
                            Toast.makeText(MapsActivity.this, "" + finalAddress, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(MapsActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            //=====add a marker on your location in map code END here====//
        }

        //===Restrict map to only for karachi city ===
        if (mMap != null) {
            Double bottomBoundary = KARACHI_LAT - 0.3;
            Double leftBoundary = KARACHI_LNG - 0.3;
            Double topBoundary = KARACHI_LAT + 0.3;
            Double rightBoundary = KARACHI_LNG + 0.3;

            LatLngBounds KARACHI_BOUNDS = new LatLngBounds(
                    new LatLng(bottomBoundary, leftBoundary),
                    new LatLng(topBoundary, rightBoundary)
            );
            mMap.setLatLngBoundsForCameraTarget(KARACHI_BOUNDS);

        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkGps();
                return true;
            }
        });
    }

    //  @Override
//    public void onMapLongClick(LatLng latLng) {
//        mMap.addMarker(new MarkerOptions().position(latLng));
//        try {
//            List<Address> addresses=geo.getFromLocation(latLng.latitude,latLng.latitude,1);
//            if (addresses.size() > 0){
//                Address address=addresses.get(0);
//                String streetAddress=address.getAddressLine(1);
//                mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title(streetAddress)
//                        .draggable(true)
//                );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//

    //===drag a marker on map from one location to another code start from here==//
    @Override
    public void onMarkerDragStart(Marker marker)
    {
      //  Toast.makeText(this, "start"+marker, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
       // Toast.makeText(this, "start"+marker, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng latLng=marker.getPosition();

        try {
            if (geo == null)
                geo = new Geocoder(MapsActivity.this, Locale.getDefault());
            List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (address.size() > 0) {
                String fullAddress= address.get(0).getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(fullAddress)
                );
            }
        } catch (IOException ex) {
            if (ex != null)
                Toast.makeText(MapsActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        //===drag a marker on map from one location to another code END here==//

    }
    private void PickAddress()
    {
        Intent addressIntent=new Intent(MapsActivity.this,AddPost.class);
        addressIntent.putExtra("MapAddress",finalAddress);
        startActivity(addressIntent);
        finish();
    }

    //=======method for checking location permission start from here=====//
    private void checkPermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(new PermissionListener() {
 @Override
  public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
 {
       isPermissionGranted=true;
      Toast.makeText(MapsActivity.this, "Permission Granted !!", Toast.LENGTH_SHORT).show();
 }

   @Override
  public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse)
   {
       Intent intent= new Intent();
       intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
       Uri uri=Uri.fromParts("package",getPackageName(),"");
       intent.setData(uri);
       startActivity(intent);
       finish();
   }

  @Override
   public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
      permissionToken.continuePermissionRequest();
   }
   }).check();
    }
    //=======method for checking location permission End here=====//
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapsActivity.this,AddPost.class));
        super.onBackPressed();
    }

//==search location on map coding start from here===
public void searchLocation(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
        Geocoder geocoder = new Geocoder(this);
        try {
        addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
        e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        if (addressList.size() > 0){
            String searchAddress=addressList.get(0).getAddressLine(0);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(location)
                .draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)
        );

            finalAddress=searchAddress;
            Toast.makeText(MapsActivity.this, ""+finalAddress, Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
        }
        }
    }//==search location on map coding End here===

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101){
            if (requestCode==RESULT_OK){
                Toast.makeText(MapsActivity.this, "Gps is enable", Toast.LENGTH_SHORT).show();
            }
            if (requestCode==RESULT_CANCELED){
                Toast.makeText(MapsActivity.this, "Denied Gps enable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //==check Gps permission==//
    private void checkGps() {
        locationRequest=LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse>locationSettingsResponseTask= LocationServices
                .getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response=task.getResult(ApiException.class);
                    Toast.makeText(MapsActivity.this, "Gps is already enable", Toast.LENGTH_SHORT).show();

                    //request location from device
                } catch (ApiException e) {
                    if (e.getStatusCode()== LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                        ResolvableApiException resolvableApiException=(ResolvableApiException)e;
                        try {
                            resolvableApiException.startResolutionForResult(MapsActivity.this,101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }

                    if (e.getStatusCode()==LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE){
                        Toast.makeText(MapsActivity.this, "Setting not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}