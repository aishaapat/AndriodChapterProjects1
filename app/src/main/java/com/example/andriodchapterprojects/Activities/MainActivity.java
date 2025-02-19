package com.example.andriodchapterprojects.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andriodchapterprojects.DB.Contact;
import com.example.andriodchapterprojects.DB.ContactDataSource;
import com.example.andriodchapterprojects.Fragments.DatePickerDialog;
import com.example.andriodchapterprojects.R;

import java.sql.SQLException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {
    private Contact currentContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initListButton();
        initMapButton();
        initSettingsButton();
        initToggleButton();
        Bundle extras=getIntent().getExtras();
        if(extras !=null) initContact(extras.getInt("contactid"));
        else currentContact=new Contact();
        setForEditing(false);
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();

    }
    public void initListButton(){
        ImageButton ibList=findViewById(R.id.contactslistbutton);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ContactListActivty.class);
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
                Intent intent=new Intent(MainActivity.this, MapActivity.class);
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

                Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initToggleButton(){
        final ToggleButton edit=(ToggleButton)findViewById(R.id.editbutton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditing(edit.isChecked());
            }
        });
    }
    private void setForEditing(boolean enabled){
        EditText editName=findViewById(R.id.editName);
        EditText editAddress=findViewById(R.id.editAddress);
        EditText editCity=findViewById(R.id.editCity);
        EditText editState=findViewById(R.id.editState);
        EditText editZipcode=findViewById(R.id.editZipcode);
        EditText phone=findViewById(R.id.editHome);
        EditText cell=findViewById(R.id.editCell);
        EditText editEmail=findViewById(R.id.editEmail);
        Button buttonchange=findViewById(R.id.btnBirthday);
        Button buttonsave=findViewById(R.id.savebutton);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipcode.setEnabled(enabled);
        phone.setEnabled(enabled);
        cell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonchange.setEnabled(enabled);
        buttonsave.setEnabled(enabled);
        if(enabled){
            editName.requestFocus();
        }

    }
    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.btnBirthday);
        changeDate.setOnClickListener(v -> {
            // Get the current date as the default selected date
            Calendar calendar = Calendar.getInstance();
            // Pass the current date to the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(calendar);
            datePickerDialog.show(getSupportFragmentManager(), "date_picker");
        });
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime)
    {
        // Set the selected date in your TextView
        TextView birthDay = findViewById(R.id.textBirthday);
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentContact.setBirthday(selectedTime);
    }
    private void initTextChangedEvents()
    {
        final EditText econtactName=findViewById(R.id.editName);
        econtactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setContactName(econtactName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText eStreetAdress=findViewById(R.id.editAddress);
        eStreetAdress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setStreetAddress(eStreetAdress.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //repeating for all edit texts
        final EditText eCity=findViewById(R.id.editCity);
        eCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setCity(eCity.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText eState=findViewById(R.id.editState);
        eState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setState(eState.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText eZipcode=findViewById(R.id.editZipcode);
        eZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setZipCode(eZipcode.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText eHomecell=findViewById(R.id.editHome);
        eHomecell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setPhoneNumber(eHomecell.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final TextView eCell=findViewById(R.id.editCell);
        eCell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.setCellNumber(eCell.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final TextView eMail=findViewById(R.id.editEmail);
        eMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentContact.seteMail(eMail.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void initSaveButton(){
        Button saveBtn=findViewById(R.id.savebutton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDataSource ds=new ContactDataSource(MainActivity.this);
                boolean wasSuccess;

                try{
                    ds.open();
                    if(currentContact.getContactID()==-1){
                        wasSuccess=ds.insertContact(currentContact);
                    }
                    else{
                        //these methods return a boolean
                        wasSuccess=ds.updateContact(currentContact);
                    }
                    ds.close();

                } catch (SQLException e) {
                    wasSuccess=false;

                }
                if(wasSuccess){
                    ToggleButton edit=findViewById(R.id.editbutton);
                    int newId=ds.getLastContactID();

                    currentContact.setContactID(newId);
                    edit.toggle();
                    setForEditing(false);
                }

            }
        });
    }

    private void initContact(int id){
        ContactDataSource ds=new ContactDataSource(MainActivity.this);
        try{
            ds.open();
            currentContact=ds.getSpecificContact(id);
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this,"Load Contact Failed",Toast.LENGTH_LONG).show();
        }
        EditText editName=findViewById(R.id.editName);
        EditText editAddress=findViewById(R.id.editAddress);
        EditText editCity=findViewById(R.id.editCity);
        EditText editState=findViewById(R.id.editState);
        EditText editZip=findViewById(R.id.editZipcode);
        EditText editCell=findViewById(R.id.editCell);
        EditText editPhone=findViewById(R.id.editHome);
        EditText editEmail=findViewById(R.id.editEmail);
        TextView birth=findViewById(R.id.textBirthday);

        //set texts now

        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editZip.setText(currentContact.getZipCode());
        editCell.setText(currentContact.getCellNumber());
        editPhone.setText(currentContact.getPhoneNumber());
        editEmail.setText(currentContact.geteMail());
        birth.setText(DateFormat.format("MM/dd/yyyy",currentContact.getBirthday().getTimeInMillis()).toString());
    }


}
