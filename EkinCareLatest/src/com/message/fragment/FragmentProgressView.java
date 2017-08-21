package com.message.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ekincare.R;
import com.message.custominterface.HorizontalListItemClickEvent;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentProgressView extends Fragment
{
    private View createView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentProgressView() {
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
        createView = inflater.inflate(R.layout.circular_progressbar, container, false);

        ProgressBar progressBar = (ProgressBar) createView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        return  createView;
    }

}
