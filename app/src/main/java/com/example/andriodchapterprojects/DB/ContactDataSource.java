package com.example.andriodchapterprojects.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import java.sql.SQLException;


public class ContactDataSource
{
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource(Context context){
        dbHelper=new ContactDBHelper(context);
    }
    public void open() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }
    public  void close() {
        dbHelper.close();
    }
    public boolean insertContact(Contact c){
        boolean didSucceed=false;
        try{
            ContentValues intialValue=new ContentValues();

            intialValue.put("contactname",c.getContactName());
            intialValue.put("streetaddress",c.getStreetAddress());
            intialValue.put("city",c.getCity());
            intialValue.put("state",c.getState());
            intialValue.put("zipcode",c.getZipCode());
            intialValue.put("phonenumber",c.getPhoneNumber());
            intialValue.put("cellnumber",c.getCellNumber());
            intialValue.put("email",c.geteMail());
            intialValue.put("birthday",String.valueOf(c.getBirthday().getTimeInMillis()));
            didSucceed=database.insert("contact",null,intialValue)>0;
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return didSucceed;
    }
    public boolean updateContact (Contact c){
        boolean didSucceed=false;
        try {
            int rowId=c.getContactID();
            ContentValues intialValue = new ContentValues();
            intialValue.put("contactname", c.getContactName());
            intialValue.put("streetaddress", c.getStreetAddress());
            intialValue.put("city", c.getCity());
            intialValue.put("state", c.getState());
            intialValue.put("zipcode", c.getZipCode());
            intialValue.put("phonenumber", c.getPhoneNumber());
            intialValue.put("cellnumber", c.getCellNumber());
            intialValue.put("email", c.geteMail());
            intialValue.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            didSucceed=database.update("contact",intialValue,"_id="+rowId,null)>0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return didSucceed;
    }
}
