package com.example.andriodchapterprojects.Activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andriodchapterprojects.R;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity {

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
    private void initGetLocationButton(){
        EditText editAddress = (EditText) findViewById(R.id.editAddress);
        EditText editCity=(EditText) findViewById(R.id.editCity);
        EditText editSate=(EditText) findViewById(R.id.editState);
        EditText editZip=(EditText) findViewById(R.id.editZipcode);

        String address=editAddress.getText().toString()+", "+editCity.getText().toString()+", "+editSate.getText().toString()+", "+editZip.getText().toString();
        List<Address> addresses=null;
        Geocoder geo=new Geocoder(MapActivity.this);
        try{
            addresses=geo.getFromLocationName(address,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView txtLatitude=(TextView) findViewById(R.id.textLatitude);
        TextView txtLongitude=(TextView) findViewById(R.id.textLongitude);
        txtLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
        txtLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));
    }
}