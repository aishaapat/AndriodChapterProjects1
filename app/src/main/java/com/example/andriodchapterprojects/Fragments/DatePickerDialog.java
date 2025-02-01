package com.example.andriodchapterprojects.Fragments;
import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;


import com.example.andriodchapterprojects.R;

import java.util.Calendar;


public class DatePickerDialog  extends DialogFragment
{
    Calendar selectedDate;





    public interface SaveDateListener {
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog(Calendar selectedDate){
        this.selectedDate=selectedDate;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the layout for the Calendar Dialog
        View view = inflater.inflate(R.layout.select_date, null);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button btnSave = view.findViewById(R.id.buttonSelect);
        Button cancel=view.findViewById(R.id.buttonCancel);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(year,month,dayOfMonth);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(selectedDate);
            }
        });

        cancel.setOnClickListener(v->{
            getDialog().dismiss();
        });




        // Set the view for the dialog and create it
        builder.setView(view);
        return builder.create();

    }

    private void saveItem(Calendar selectedTime){
        SaveDateListener activity=(SaveDateListener) getActivity();
        activity.didFinishDatePickerDialog(selectedTime);
        getDialog().dismiss();
    }


}
