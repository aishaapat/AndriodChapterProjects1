package com.example.andriodchapterprojects.Activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andriodchapterprojects.DB.Contact;
import com.example.andriodchapterprojects.DB.ContactDataSource;
import com.example.andriodchapterprojects.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gmap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    RadioButton rbNormal=findViewById(R.id.radioButtonNormal);
    RadioButton satelitebtn=findViewById(R.id.radioButtonSatellite);
    ArrayList<Contact> contacts = new ArrayList<>();
    Contact currentContact = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        Bundle extras=getIntent().getExtras();
        try{
            ContactDataSource ds=new ContactDataSource(MapActivity.this);
            ds.open();
            if(extras !=null){
                currentContact=ds.getSpecificContact(extras.getInt("contactid"));
            }
            else{
                contacts=ds.getContacts("contactname","ASC");
            }
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this,"Contact(s) could not be retrieved",Toast.LENGTH_LONG).show();
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_contact_map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initListButton();
        initSettingsButton();
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequest();
        createLocationCallback();
        initMapTypeButtons();


    }


    public void initListButton(){
        ImageButton ibList=findViewById(R.id.contactslistbutton);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                Intent intent=new Intent(MapActivity.this, ContactListActivty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initSettingsButton(){
        ImageButton ibList=findViewById(R.id.settingsicon);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                Intent intent=new Intent(MapActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void createLocationRequest(){
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(5000)
                .build();

    }

    private void createLocationCallback(){
        locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult==null) return;
                for(Location location : locationResult.getLocations()){
                    Toast.makeText(getBaseContext(),"Lat: " + location.getLatitude()+ " Long: "+ location.getLongitude()+" Accuracy" +
                            ": "+ location.getAccuracy(),Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, null);
        gmap.setMyLocationEnabled(true);
    }
    private void stopLocationUpdates () {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Point size=new Point();
        WindowManager w = getWindowManager();
        int measuredWidth=size.x;
        int measureHeight=size.y;
        rbNormal.setChecked(true);
        if(contacts.size()>0){
            LatLngBounds.Builder builder=new LatLngBounds.Builder();
            for(int i=0;i<contacts.size();i++){
                currentContact=contacts.get(i);
                Geocoder geo=new Geocoder(this);
                List<Address> addresses=null;
                String address= currentContact.getStreetAddress()+", "+ currentContact.getCity()+" , "+currentContact.getState()+" , "+currentContact.getZipCode();
                try{
                    addresses=geo.getFromLocationName(address,1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                LatLng point=new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                builder.include(point);
                gmap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
            }
            gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),measuredWidth,measureHeight,450));
        }
        else{
            if (currentContact!= null){
                Geocoder geocoder=new Geocoder(this);
                List<Address> addresses=null;
                String address= currentContact.getStreetAddress()+", "+ currentContact.getCity()+" , "+currentContact.getState()+" , "+currentContact.getZipCode();
                try{
                    addresses=geocoder.getFromLocationName(address,1);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                LatLng point=new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                gmap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,16));
            }
            else{
                AlertDialog alertDialog=new AlertDialog.Builder(MapActivity.this).create();
                alertDialog.setTitle("No data");
                alertDialog.setMessage("no data available");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(MapActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            MapActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )) {
                        Snackbar.make(findViewById(R.id.activity_contact_map),
                                        "MyContactList requires this permission to locate " +
                                                "your contacts", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ActivityCompat.requestPermissions(
                                                MapActivity.this,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                PERMISSION_REQUEST_LOCATION
                                        );

                                    }
                                })
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(
                                MapActivity.this,
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error requesting permission",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult (int requestCode, String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

    }

    private void initMapTypeButtons() {
        RadioGroup rgMapType = findViewById(R.id.radioGroupMapType);
        rgMapType.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
                if (rbNormal.isChecked()) {
                    gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });
    }
}