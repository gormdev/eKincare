package com.message.fragment;

import android.app.Activity;
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
import android.widget.ProgressBar;

import com.ekincare.R;
import com.message.custominterface.BottomSheetFragmentInterface;
import com.message.custominterface.HorizontalListItemClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentHraFamilyHistoryView extends Fragment
{
    private View createView;
    public CheckBox checkBoxHyperTension,checkBoxCardiac,checkBoxNone, checkBoxCancer,checkBoxDiabetes,checkBoxBP;


    BottomSheetFragmentInterface bottomSheetFragmentInterface;
    String result = "";
    List<String> familyDisease;
    boolean isParents;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentHraFamilyHistoryView() {
    }

    public FragmentHraFamilyHistoryView(BottomSheetFragmentInterface bottomSheetFragmentInterface, boolean isParents)
    {
        this.bottomSheetFragmentInterface =bottomSheetFragmentInterface;
        this.isParents = isParents;
        familyDisease = new ArrayList<>();
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
        createView = inflater.inflate(R.layout.item_message_fragment_hra_family_history, container, false);

        checkBoxHyperTension = (CheckBox) createView.findViewById(R.id.hypertension_checkboxFather);
        checkBoxCardiac = (CheckBox) createView.findViewById(R.id.cardio_disease_checkboxFather);
        checkBoxNone = (CheckBox) createView.findViewById(R.id.none_checkboxFather);
        checkBoxCancer = (CheckBox) createView.findViewById(R.id.cancer_checkboxFather);
        checkBoxDiabetes = (CheckBox) createView.findViewById(R.id.diabetes_checkboxFather);
        checkBoxBP = (CheckBox) createView.findViewById(R.id.bloodpressure_checkboxFather);


        if(isParents){
            checkBoxCancer.setVisibility(View.VISIBLE);
            checkBoxCardiac.setVisibility(View.VISIBLE);
            checkBoxBP.setVisibility(View.GONE);
        }else{
            checkBoxCancer.setVisibility(View.GONE);
            checkBoxCardiac.setVisibility(View.GONE);
            checkBoxBP.setVisibility(View.VISIBLE);
        }

        checkBoxBP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxNone.setChecked(false);
                    familyDisease.add("Blood Pressure");
                    sendDataToFragment();
                }else{
                    familyDisease.remove("Blood Pressure");
                    sendDataToFragment();
                }
            }
        });

        checkBoxCancer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxNone.setChecked(false);
                    familyDisease.add("Cancer");
                    sendDataToFragment();
                }else{
                    familyDisease.remove("Cancer");
                    sendDataToFragment();
                }
            }
        });
        checkBoxDiabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxNone.setChecked(false);
                    familyDisease.add("Diabetics");
                    sendDataToFragment();
                }else{
                    familyDisease.remove("Diabetics");
                    sendDataToFragment();
                }
            }
        });

        checkBoxCardiac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxNone.setChecked(false);
                    familyDisease.add("Heart Condition");
                    sendDataToFragment();
                }else{
                    familyDisease.remove("Heart Condition");
                    sendDataToFragment();
                }
            }
        });
        checkBoxHyperTension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxNone.setChecked(false);
                    familyDisease.add("Hypertension");
                    sendDataToFragment();
                }else{
                    familyDisease.remove("Hypertension");
                    sendDataToFragment();
                }
            }
        });


        checkBoxNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxCancer.setChecked(false);
                    checkBoxCardiac.setChecked(false);
                    checkBoxBP.setChecked(false);
                    checkBoxDiabetes.setChecked(false);
                    checkBoxHyperTension.setChecked(false);
                    sendDataToFragment();
                    familyDisease.clear();
                }
            }
        });


        return  createView;
    }

    private void sendDataToFragment(){
        if(familyDisease.size()>0){
            result = TextUtils.join(",",familyDisease);
        }
        else{
            result = "None";
        }

        bottomSheetFragmentInterface.onBottonFragmentItemClick(result);
    }
}
