package com.example.andriodchapterprojects;
import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;



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

        cancel.setOnClickListener(v->{
            getDialog().dismiss();
        });

        // Set up the save button click listener
        btnSave.setOnClickListener(v -> {
            long selectedDateInMillis = calendarView.getDate();

            // Create a Calendar object using the milliseconds
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTimeInMillis(selectedDateInMillis);
            saveItem(selectedDate);
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
