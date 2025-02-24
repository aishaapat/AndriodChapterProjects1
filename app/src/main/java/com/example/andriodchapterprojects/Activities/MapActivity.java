package com.example.andriodchapterprojects.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button locationButton=(Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGetLocationButton();
            }
        });
    }
    @Override
    public void onPause(){

        super.onPause();
        try{
            locationManager.removeUpdates(gpsListener);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initGetLocationButton()
    {
        try {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        Snackbar.make(findViewById(R.id.activity_map), "MyContactList Requires this permission to locate your contacts", Snackbar.LENGTH_INDEFINITE).setAction
                                ("OK", new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                                                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                            }


                                        }
                                ).show();
                    }
                }
            }
        }        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }



    private void startLocationUpdates(){
        if(Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);

                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

        } catch (Exception e) {
            Toast.makeText(getBaseContext(),"Error,Location not available",Toast.LENGTH_LONG).show();
        }
    }
}