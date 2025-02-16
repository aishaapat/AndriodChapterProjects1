package com.example.andriodchapterprojects.LayoutChanges;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andriodchapterprojects.R;

import java.util.ArrayList;


import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter
{
    private ArrayList<String> contactData;
    private View.OnClickListener mOnItemClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ContactViewHolder cvh=(ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position));
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }

    public void setmOnItemClickListener(View.OnClickListener itemClickListener){
         mOnItemClickListener=itemClickListener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewContact;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textViewName);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getContactTextView(){
            return textViewContact;
        }
    }
    public ContactAdapter(ArrayList<String> arrayList)
    {
        contactData=arrayList;

    }
}







