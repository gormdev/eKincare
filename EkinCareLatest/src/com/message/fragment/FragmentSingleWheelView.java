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

import com.ekincare.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.message.custominterface.BottomSheetFragmentInterface;
import com.message.custominterface.HorizontalListItemClickEvent;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentSingleWheelView extends Fragment
{
    View createView;
    private WheelView wheelView;
    String unit;
    BottomSheetFragmentInterface bottomSheetFragmentInterface;
    FloatingActionButton fab;
    String weelWeightScrollvalue;
    boolean isEnable = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentSingleWheelView(BottomSheetFragmentInterface bottomSheetFragmentInterface) {
        this.bottomSheetFragmentInterface =bottomSheetFragmentInterface;
    }

    public FragmentSingleWheelView() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ArrayList<? extends String> newList = getArguments().getParcelableArrayList("list");
        unit = getArguments().getString("unit");
        System.out.println("FragmentSingleWheelView.onCreateView newList="+newList.toString());
        createView = inflater.inflate(R.layout.weel_scroll_dialog_weight, container, false);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#317af2");
        style.textColor = getActivity().getResources().getColor(R.color.card_first);
        style.selectedTextSize = 25;
        style.textSize = 18;
        style.selectedTextZoom = 2.0f;
        style.backgroundColor =  Color.parseColor("#f7f7f7");
        style.textAlpha = 1;
        style.holoBorderColor = Color.parseColor("#d2d2d2");

        fab = (FloatingActionButton) createView.findViewById(R.id.weel_weight_floating);
        fab.setVisibility(View.GONE);

        wheelView = (WheelView) createView.findViewById(R.id.wheelview_weight);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(getActivity()));
        wheelView.setSkin(WheelView.Skin.Holo);
        try{
            wheelView.setWheelData(newList);
        }catch (Exception e){
            e.printStackTrace();
        }
        wheelView.setExtraText(unit, getActivity().getResources().getColor(R.color.card_first), 50, 120);
        wheelView.setStyle(style);
        wheelView.setLoop(false);
        wheelView.setWheelClickable(true);
        wheelView.setWheelSize(5);
        if(newList.size()<10){
            wheelView.setSelection(newList.size()/2);
        }else if(newList.size()<20){
            wheelView.setSelection(12);
        }else if(newList.size()<30){
            wheelView.setSelection(20);
        }else if(newList.size()<50){
            wheelView.setSelection(30);
        }else if(newList.size()<100){
            wheelView.setSelection(50);
        }else if(newList.size()<150){
            wheelView.setSelection(100);
        }else if(newList.size()<200){
            wheelView.setSelection(100);
        }else if(newList.size()<300){
            wheelView.setSelection(50);
        }
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                weelWeightScrollvalue = o.toString();
                if(isEnable){
                    bottomSheetFragmentInterface.onBottonFragmentItemClick(weelWeightScrollvalue);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEnable = false;
                bottomSheetFragmentInterface.onBottonFragmentItemClick(weelWeightScrollvalue);
            }
        });
        return  createView;
    }

    public void setWheelButtonVisible() {
        fab.setVisibility(View.VISIBLE);
        isEnable = false;
    }

    public boolean isEnableTrue(){return isEnable;}
}
