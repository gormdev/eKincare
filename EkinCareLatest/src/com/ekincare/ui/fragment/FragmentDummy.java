package com.ekincare.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekincare.R;

/**
 * Created by RaviTejaN on 21-07-2016.
 */
public class FragmentDummy extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View mView = inflater.inflate(R.layout.fragment_dummy_layout, container, false);

        return mView;
    }

}
