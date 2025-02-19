package com.example.andriodchapterprojects.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;


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

    public ArrayList<Contact> getContacts(){
        ArrayList<Contact> contacts=new ArrayList<>();

        try{
            String query="Select * from contact";
            Cursor cursor=database.rawQuery(query,null);
            Contact newContact;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newContact=new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));
                Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();

            }
            cursor.close();
        } catch (Exception e) {
            contacts=new ArrayList<>();
        }
        return contacts;
    }



}
