package com.example.baggins.moviesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

public class DateOptionActivity extends Activity implements View.OnClickListener {

    private static final int DEFAULT_START_DATE =1950;
    private static final int RESULT_UNDEFINED = 564;
    private NumberPicker startDatePicker, endDatePicker;
    private Button submitButton, cancelButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_option);
        initializeActivitiesWidgets();
        startDatePicker.setOnValueChangedListener(startDatePickerChangeListener());
        endDatePicker.setOnValueChangedListener(endDateChangePickerListener());
        intent = getIntent();
        setDataFromIntent();
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        intent.putExtra("startDate", startDatePicker.getValue());
        intent.putExtra("endDate", endDatePicker.getValue());
        switch (v.getId()) {
            case R.id.submit_button: { setResult(RESULT_OK, intent); break;}
            case R.id.cancel_button: { setResult(RESULT_UNDEFINED, intent); break;}
        }
        finish();
    }

    void initializeActivitiesWidgets() {
        submitButton = (Button) findViewById(R.id.submit_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        startDatePicker = (NumberPicker) findViewById(R.id.start_date_picker);
        endDatePicker = (NumberPicker) findViewById(R.id.end_date_picker);
    }

    NumberPicker.OnValueChangeListener startDatePickerChangeListener() {
        return new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                endDatePicker.setMinValue(newVal);
            }
        };
    }

    NumberPicker.OnValueChangeListener endDateChangePickerListener() {
        return new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startDatePicker.setMaxValue(newVal);
            }
        };
    }

    void setDataFromIntent(){
        Integer startDate = intent.getIntExtra("startDate", 0);
        Integer endDate = intent.getIntExtra("endDate", 0);
        if(startDate == 0)
            setDatePickers(DEFAULT_START_DATE, Calendar.getInstance().get(Calendar.YEAR));
        else
            setDatePickers(startDate, endDate);
    }

    void setDatePickers(Integer startDate, Integer endDate) {
        startDatePicker.setMaxValue(endDate);
        endDatePicker.setMinValue(startDate);
        startDatePicker.setMinValue(DEFAULT_START_DATE);
        endDatePicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);
    }

}
