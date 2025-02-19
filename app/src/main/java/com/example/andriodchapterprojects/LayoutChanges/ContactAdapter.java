package com.example.andriodchapterprojects.LayoutChanges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andriodchapterprojects.DB.Contact;
import com.example.andriodchapterprojects.DB.ContactDataSource;
import com.example.andriodchapterprojects.R;

import java.util.ArrayList;


import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter
{
    private ArrayList<Contact> contactData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getTextphoneView().setText(contactData.get(position).getPhoneNumber());

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
    }
    public void setDelete(boolean b){
        isDeleting=b;
    }
    private void deleteItem(int position){
        Contact contact=contactData.get(position);
        ContactDataSource ds=new ContactDataSource(context);
        try{
            ds.open();
            boolean didDelete=ds.deleteContact(contact.getContactID());
            ds.close();
            if (didDelete){
                contactData.remove(position);
                notifyDataSetChanged();
            }
            else Toast.makeText(context,"Delete failed",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context,"Delete failed",Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public int getItemCount() {
        return contactData.size();
    }


    public void setmOnItemClickListener(View.OnClickListener itemClickListener){
         this.mOnItemClickListener=itemClickListener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewContact;
        public TextView textphone;
        public Button deleteButton;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textphone=itemView.findViewById(R.id.textPhoneNumber);
            deleteButton=itemView.findViewById(R.id.buttonDeleteContact);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getContactTextView(){
            return textViewContact;
        }
        public TextView getTextphoneView(){
            return textphone;
        }
        public Button getDeleteButton(){
            return deleteButton;
        }
    }
    public ContactAdapter(ArrayList<Contact> arrayList, View.OnClickListener onClickListener, Context context)
    {
        contactData=arrayList;
        mOnItemClickListener=onClickListener;
        this.context=context;
    }

}







