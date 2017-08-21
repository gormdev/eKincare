package com.oneclick.ekincare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ekincare.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by RaviTejaN on 20-10-2016.
 */

@SuppressLint("ValidFragment")
public class DialogBloodGlucosePicker extends DialogFragment {
    private Context context;
    private WheelView secondWheelView;
    ArrayList<String> list;
    DialogBloodGlcousInterface dialogBloodGlcousInterface;
    FloatingActionButton wheelWeight;
    int weelWeightScrollPosition;
    String weelWeightScrollvalue;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dialogBloodGlcousInterface = (DialogBloodGlcousInterface) activity;

    }

    @SuppressLint("ValidFragment")
    public DialogBloodGlucosePicker(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        context = getActivity();
        Window window = getDialog().getWindow();
        ViewGroup.LayoutParams attributes = window.getAttributes();
        //must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialog = inflater.inflate(R.layout.weel_scroll_dialog_weight, container);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#fcc633");
        style.textColor = Color.parseColor("#F5F5F5");
        style.selectedTextSize = 25;
        style.textSize = 18;
        style.selectedTextZoom = 2.0f;
        style.backgroundColor =  Color.parseColor("#0288ce");
        style.textAlpha = 1;
        style.holoBorderColor = Color.parseColor("#F5F5F5");
        wheelWeight = (FloatingActionButton) dialog.findViewById(R.id.weel_weight_floating);
        secondWheelView = (WheelView) dialog.findViewById(R.id.wheelview_weight);
        secondWheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        secondWheelView.setSkin(WheelView.Skin.Holo);
        secondWheelView.setWheelData(list);
        secondWheelView.setStyle(style);
        secondWheelView.setExtraText("  mg/dl", Color.parseColor("#ffffff"), 50, 120);
        secondWheelView.setLoop(false);
        secondWheelView.setWheelClickable(true);
        secondWheelView.setWheelSize(27);
        secondWheelView.setSelection(30);
        secondWheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                dialogBloodGlcousInterface.setBloodGlcouse(o.toString(), position);
                dismiss();
            }
        });
        secondWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                weelWeightScrollPosition = position;
                weelWeightScrollvalue = o.toString();
                dialogBloodGlcousInterface.setBloodGlcouse(o.toString(), position);
            }
        });
        wheelWeight.setVisibility(View.VISIBLE);

        wheelWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBloodGlcousInterface.setBloodGlcouse(weelWeightScrollvalue, weelWeightScrollPosition);
                dismiss();
            }
        });


        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }
}

