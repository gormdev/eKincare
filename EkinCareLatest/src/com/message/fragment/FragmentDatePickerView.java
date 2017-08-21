package com.message.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ekincare.R;
import com.message.custominterface.BottomSheetFragmentInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentDatePickerView extends Fragment
{
    private View createView;

    BottomSheetFragmentInterface bottomSheetFragmentInterface;
    String result = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentDatePickerView() {
    }

    public FragmentDatePickerView(BottomSheetFragmentInterface bottomSheetFragmentInterface)
    {
        this.bottomSheetFragmentInterface =bottomSheetFragmentInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRetainInstance(true);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.bottom_sheet_date_picker, container, false);

        DatePicker datePicker = (DatePicker) createView.findViewById(R.id.datepicker);


       /* Calendar maxCalender = Calendar.getInstance();
        int curentYear = maxCalender.get(Calendar.YEAR);
        curentYear = curentYear - 18;
        maxCalender.set(curentYear, maxCalender.get(Calendar.MONTH), maxCalender.get(Calendar.DAY_OF_MONTH) + 1);
        datePicker.setMaxDate(maxCalender.getTimeInMillis());


        final SimpleDateFormat dateViewFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.YEAR,calendar.MONTH,calendar.DAY_OF_MONTH, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar choosenDate = Calendar.getInstance();
                choosenDate.set(year, monthOfYear, dayOfMonth);
                bottomSheetFragmentInterface.onBottonFragmentItemClick(dateViewFormatter.format(choosenDate.getTime()));
            }
        });*/


        Calendar currentCalender = Calendar.getInstance();
        int curentYear = currentCalender.get(Calendar.YEAR);
        curentYear = curentYear - 18;
        currentCalender.set(curentYear, currentCalender.get(Calendar.MONTH), currentCalender.get(Calendar.DAY_OF_MONTH) + 1);
        datePicker.setMaxDate(currentCalender.getTimeInMillis());

        //currentCalender.set(1980, currentCalender.get(Calendar.MONTH), currentCalender.get(Calendar.DAY_OF_MONTH) + 1);

        datePicker.updateDate(currentCalender.get(Calendar.YEAR), currentCalender.get(Calendar.MONTH),currentCalender.get(Calendar.DAY_OF_MONTH) + 1);

        int year = currentCalender.get(Calendar.YEAR);
        int month = currentCalender.get(Calendar.MONTH);
        int day = currentCalender.get(Calendar.DAY_OF_MONTH);

        //datePicker.setMinDate(currentCalender.get(Calendar.YEAR) - 18);
        final SimpleDateFormat dateViewFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        // Initialize datepicker in dialog atepicker
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar choosenDate = Calendar.getInstance();
                choosenDate.set(year, monthOfYear, dayOfMonth);
                bottomSheetFragmentInterface.onBottonFragmentItemClick(dateViewFormatter.format(choosenDate.getTime()));


            }
        });




        return  createView;
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
