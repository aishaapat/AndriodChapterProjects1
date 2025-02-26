package com.example.andriodchapterprojects.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andriodchapterprojects.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    LocationManager locationManager;
    Location currentBestLocation;

    LocationListener gpsListener;
    LocationListener networkListener;
    final int PERMISSION_REQUEST_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_contact_map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initGetLocationButton();
        initGetNetButton();
        initSettingsButton();
        initListButton();
    }


    private void startLocationUpdates() {
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
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else  {
                    Toast.makeText(MapActivity.this, "MyContactList will not locate your contacts",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(
                Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            return;
        }

        try {
            locationManager.removeUpdates(networkListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///
    private void initGetNetButton() {

        Button button=findViewById(R.id.buttonGetnet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){
                    return;
                }
                startLocationUpdates();
                try{
                    locationManager=(LocationManager)getBaseContext().getSystemService(Context.LOCATION_SERVICE);
                    gpsListener=new LocationListener() {

                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            if (isBetterLocation(location))currentBestLocation=location;
                            TextView txtLat=(TextView) findViewById(R.id.textLatitude);
                            TextView txtLong=(TextView) findViewById(R.id.textLongitude);
                            TextView txtAccur=(TextView) findViewById(R.id.textAccuracy);
                            txtLat.setText(String.valueOf(location.getLatitude()));
                            txtLong.setText(String.valueOf(location.getLongitude()));
                            txtAccur.setText(String.valueOf(location.getAccuracy()));
                        }

                        @Override
                        public void onLocationChanged(@NonNull List<Location> locations) {


                            LocationListener.super.onLocationChanged(locations);
                        }

                        @Override
                        public void onFlushComplete(int requestCode) {
                            LocationListener.super.onFlushComplete(requestCode);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            LocationListener.super.onStatusChanged(provider, status, extras);
                        }

                        @Override
                        public void onProviderEnabled(@NonNull String provider) {
                            LocationListener.super.onProviderEnabled(provider);
                        }

                        @Override
                        public void onProviderDisabled(@NonNull String provider) {
                            LocationListener.super.onProviderDisabled(provider);
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,networkListener);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),"Error, Location not availble",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isBetterLocation(Location location){
        boolean isBetter=false;
        if (currentBestLocation==null) isBetter=true;
        else if (location.getAccuracy()<= currentBestLocation.getAccuracy()) {
            isBetter=true;
        } else if (location.getTime()-currentBestLocation.getTime()> 5*60*1000) {
            isBetter=true;
        }
        return isBetter;
    }

    private void initGetLocationButton(){
        Button locate=(Button) findViewById(R.id.buttonGetLocation);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editAddress=(EditText) findViewById(R.id.editAddress);
                EditText editCity=(EditText) findViewById(R.id.editCity);
                EditText editState=(EditText) findViewById(R.id.editState);
                EditText editZip=(EditText) findViewById(R.id.editZipcode);

                String address= editAddress.getText().toString()+", "+editCity.getText().toString()+", "+editState.getText().toString()+", "+editZip.getText().toString()+", ";
                List<Address> addresses=null;
                Geocoder geo=new Geocoder(MapActivity.this);
                try{
                    addresses=geo.getFromLocationName(address,1);
                }
                catch (IOException e){e.printStackTrace();}
                TextView txtLa=(TextView) findViewById(R.id.textLatitude);
                TextView txtLong=(TextView) findViewById(R.id.textLongitude);
                txtLa.setText(String.valueOf(addresses.get(0).getLatitude()));
                txtLong.setText(String.valueOf(addresses.get(0).getLongitude()));
            }
        });
    }

    public void initListButton(){
        ImageButton ibList=findViewById(R.id.contactslistbutton);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Intent intent=new Intent(MapActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }







}