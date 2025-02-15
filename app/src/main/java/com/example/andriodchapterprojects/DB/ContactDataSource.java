package com.example.andriodchapterprojects.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;


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
    public int getLastContactID(){
        int lastId;
        try{
            String query="Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query,null);
            cursor.moveToFirst();
            lastId=cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            lastId=-1;
        }
        return lastId;
    }

    public ArrayList<String> getContactName(){
        ArrayList<String> contactNames= new ArrayList<>();
        try{
            String query="Select contactname from contact";
            Cursor cursor=database.rawQuery(query,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast());{
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            contactNames=new ArrayList<String>();
        }
        return contactNames;
    }



}
