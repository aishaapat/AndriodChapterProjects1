package com.example.andriodchapterprojects.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andriodchapterprojects.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

      initListButton();
      initMapButton();
      initSettingsButton();
      initSettings();
      initSortOrderClick();
      initSortByCLick();
    }
    public void initListButton(){
        ImageButton ibList=findViewById(R.id.contactslistbutton);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ContactListActivty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initMapButton(){
        ImageButton ibList=findViewById(R.id.mapsicon);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initSettingsButton(){
        ImageButton ibSettings = findViewById(R.id.settingsicon);
        ibSettings.setEnabled(false);
    }
    private void initSettings(){
        String sortBy=getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield","contactname");
        String sortOrder=getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).getString("sortorder","ASC");
        RadioButton rbName=findViewById(R.id.radioName);
        RadioButton rbCity=findViewById(R.id.radioCity);
        RadioButton rbBirthday=findViewById(R.id.radioBirthday);

        if(sortBy.equalsIgnoreCase("contactname")) rbName.setChecked(true);
        else if(sortBy.equalsIgnoreCase("city")) rbCity.setChecked(true);
        else rbBirthday.setChecked(true);

        RadioButton rbASC=findViewById(R.id.radioASC);
        RadioButton rbdesc=findViewById(R.id.radioDESC);
        if(sortOrder.equalsIgnoreCase("ASC"))
            rbASC.setChecked(true);
        else rbdesc.setChecked(true);

    }
    private  void initSortOrderClick(){
        RadioGroup rgSortorder=findViewById(R.id.radioGroupOrderBy);
        rgSortorder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbASC=findViewById(R.id.radioASC);
                if(rbASC.isChecked())
                {
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).edit().putString("sortorder","ASC").apply();
                }
                else{
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).edit().putString("sortorder","DESC").apply();
                }
            }
        });

    }
    private void initSortByCLick(){
        RadioGroup rgSortBy=findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbName=findViewById(R.id.radioName);
                RadioButton rbCity=findViewById(R.id.radioCity);
                if(rbName.isChecked()){
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).edit().putString("sortfield","contactname").apply();
                }
                else if(rbCity.isChecked()){
                    getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).edit().putString("sortfield","city").apply();
                }
                else getSharedPreferences("MyContactListPreferences",Context.MODE_PRIVATE).edit().putString("sortfield","birthday").apply();
            }
        });
    }
}