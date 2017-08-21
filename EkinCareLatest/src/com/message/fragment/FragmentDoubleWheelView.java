package com.message.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

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

public class FragmentDoubleWheelView extends Fragment
{
    private WheelView wheelView1, wheelView2;
    BottomSheetFragmentInterface bottomSheetFragmentInterface;
    String unit1, unit2;
    String weelHightFeetPositionValue,weelHightInchPositionValue;
    private View createView;

    boolean isEnable = true;
    private FloatingActionButton fab;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentDoubleWheelView(BottomSheetFragmentInterface bottomSheetFragmentInterface) {
        this.bottomSheetFragmentInterface =bottomSheetFragmentInterface;
    }

    public FragmentDoubleWheelView() {
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
        ArrayList<? extends String> list1 = getArguments().getParcelableArrayList("list1");
        unit1 = getArguments().getString("unit1");
        ArrayList<? extends String> list2 = getArguments().getParcelableArrayList("list2");
        unit2 = getArguments().getString("unit2");

        createView = inflater.inflate(R.layout.weel_scroll_dialog_height, container, false);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#317af2");
        style.textColor = getActivity().getResources().getColor(R.color.card_first);
        style.selectedTextSize = 25;
        style.textSize = 18;
        style.selectedTextZoom = 2.0f;
        style.backgroundColor =  getActivity().getResources().getColor(R.color.chat_box_enter_text_main_bg);
        style.textAlpha = 1;
        style.holoBorderColor = Color.parseColor("#d2d2d2");

        fab=(FloatingActionButton)createView.findViewById(R.id.weel_height_floating);
        fab.setVisibility(View.GONE);

        wheelView2 = (WheelView) createView.findViewById(R.id.wheelview_feet);
        wheelView2.setWheelAdapter(new ArrayWheelAdapter(getActivity()));
        wheelView2.setSkin(WheelView.Skin.Holo);
        wheelView2.setWheelData(list1);
        wheelView2.setStyle(style);
        wheelView2.setLoop(false);
        wheelView2.setWheelClickable(true);
        wheelView2.setWheelSize(27);
        wheelView2.setSelection(list1.size()/2);
        wheelView2.setExtraText(unit1, getActivity().getResources().getColor(R.color.card_first), 50, 120);

        wheelView2.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                weelHightFeetPositionValue=o.toString();
                if(isEnable){
                    bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
                }
            }
        });
        wheelView2.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                weelHightFeetPositionValue=o.toString();
                if(isEnable){
                    bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
                }
            }
        });

        wheelView1 = (WheelView) createView.findViewById(R.id.wheelview_inch);
        wheelView1.setWheelAdapter(new ArrayWheelAdapter(getActivity()));
        wheelView1.setSkin(WheelView.Skin.Holo);
        wheelView1.setWheelData(list2);
        wheelView1.setStyle(style);
        wheelView1.setLoop(false);
        wheelView1.setWheelClickable(true);
        wheelView1.setWheelSize(27);
        wheelView1.setSelection(list2.size()/2);
        wheelView1.setExtraText(unit2, getActivity().getResources().getColor(R.color.card_first), 50, 110);

        wheelView1.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                weelHightInchPositionValue=o.toString();
                if(isEnable){
                    bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
                }
            }
        });
        wheelView1.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                weelHightInchPositionValue=o.toString();
                if(isEnable){
                    bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
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
