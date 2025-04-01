package com.example.memolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;



public class DatePickerDialog extends DialogFragment{

    Calendar selectedDate;

    public interface SaveDateListener {
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog(){

    }

    public void saveItem(Calendar selectedTime){
        SaveDateListener activity = (SaveDateListener) getActivity();
        activity.didFinishDatePickerDialog(selectedTime);
        getDialog().dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.selected_date, container);

        getDialog().setTitle("Select Date");
        selectedDate = Calendar.getInstance();

        final CalendarView cv = view.findViewById(R.id.calendarView);
        cv.setOnDateChangeListener((calendarView, year, month, day) -> selectedDate.set(year,month,day));

        Button saveButton = view.findViewById(R.id.buttonSelect);
        saveButton.setOnClickListener(v ->
                saveItem(selectedDate)
        );

        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        return view;

    }
}
