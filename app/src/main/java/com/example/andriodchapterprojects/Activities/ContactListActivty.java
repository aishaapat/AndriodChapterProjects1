package com.example.andriodchapterprojects.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andriodchapterprojects.DB.ContactDataSource;
import com.example.andriodchapterprojects.LayoutChanges.ContactAdapter;
import com.example.andriodchapterprojects.R;

import java.sql.SQLException;
import java.util.ArrayList;

public class ContactListActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list_activty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showContact();
        initListButton();
        initMapButton();
        initSettingsButton();



    }
    public void initListButton(){
        ImageButton ibSettings = findViewById(R.id.contactslistbutton);
        ibSettings.setEnabled(false);
    }
    public void initMapButton(){
        ImageButton ibList=findViewById(R.id.mapsicon);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ContactListActivty.this, MapActivity.class);
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
                Intent intent=new Intent(ContactListActivty.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
    public void showContact() {
        ContactDataSource ds = new ContactDataSource(this);
        ArrayList<String> names;
        try{
            ds.open();;
            names=ds.getContactName();
            ds.close();
            RecyclerView contactList=findViewById(R.id.rvContacts);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
            contactList.setLayoutManager(layoutManager);
            ContactAdapter contactAdapter = new ContactAdapter(names);
            contactList.setAdapter(contactAdapter);
        }
        catch (Exception e){
            Toast.makeText(this,"Error retrieving cotnacts",Toast.LENGTH_LONG).show();
        }
    }


}
