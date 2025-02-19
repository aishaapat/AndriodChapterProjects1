package com.example.andriodchapterprojects.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andriodchapterprojects.DB.Contact;
import com.example.andriodchapterprojects.DB.ContactDataSource;
import com.example.andriodchapterprojects.LayoutChanges.ContactAdapter;
import com.example.andriodchapterprojects.R;

import java.sql.SQLException;
import java.util.ArrayList;

public class ContactListActivty extends AppCompatActivity {
    ArrayList<Contact> contacts;


    private View.OnClickListener onItemClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder=(RecyclerView.ViewHolder) v.getTag();
            int position=viewHolder.getAdapterPosition();
        int contactId=contacts.get(position).getContactID();
            Intent intent=new Intent(ContactListActivty.this,MainActivity.class);
            intent.putExtra("contactid",contactId);
            startActivity(intent);
        }
    };
    ContactAdapter contactAdapter1 = new ContactAdapter(contacts,onItemClickListener,this);

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

        initListButton();
        initMapButton();
        initSettingsButton();
        initAddContactButtob();
        initDeleteSwitch(contactAdapter1);
    }
    @Override
    public void onResume(){
        super.onResume();
        String sortBy = getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).getString("sortfield", "contactname");

        String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        ContactDataSource ds = new ContactDataSource(this);


        try {
            ds.open();
            contacts = ds.getContacts(sortBy, sortOrder);
            ds.close();

            if (contacts.size() > 0) {

                RecyclerView contactList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                contactList.setLayoutManager(layoutManager);

                ContactAdapter contactAdapter = new ContactAdapter(contacts, onItemClickListener, this);
                contactList.setAdapter(contactAdapter);


                initDeleteSwitch(contactAdapter);

            } else {
                Intent intent = new Intent(ContactListActivty.this, MainActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
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

   try{
       String sortBy=getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield","contactname");
       String sortOrder=getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder","ASC");
          ds.open();;
         contacts=ds.getContacts(sortBy,sortOrder);

          ds.close();
          RecyclerView contactList=findViewById(R.id.rvContacts);
          RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
         contactList.setLayoutManager(layoutManager);
       ContactAdapter contactAdapter = new ContactAdapter(contacts,onItemClickListener,this);
        contactList.setAdapter(contactAdapter);

       }
       catch (Exception e){
          Toast.makeText(this,"Error retrieving contacts",Toast.LENGTH_LONG).show();
       }
   }

   public void initAddContactButtob(){
       Button newContact=findViewById(R.id.buttonAddContact);
       newContact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(ContactListActivty.this,MainActivity.class);
               startActivity(intent);
           }
       });
   }

    private void initDeleteSwitch(ContactAdapter contactAdapter) {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                contactAdapter.setDelete(status);
                contactAdapter.notifyDataSetChanged();
            }
        });
    }


}
