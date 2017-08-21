package com.message.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ekincare.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.message.custominterface.HorizontalListItemClickEvent;
import com.oneclick.ekincare.DialogBloodGlcousInterface;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by RaviTejaN on 20-10-2016.
 */

@SuppressLint("ValidFragment")
public class DialogOnePicker extends DialogFragment {
    private Context context;
    private WheelView wheelView;
    String unit;
    ArrayList<String> list;
    HorizontalListItemClickEvent horizontalListItemClickEvent;
    FloatingActionButton wheelWeight;
    int weelWeightScrollPosition;
    String weelWeightScrollvalue;

    @SuppressLint("ValidFragment")
    public DialogOnePicker(Context context, ArrayList<String> list, HorizontalListItemClickEvent horizontalListItemClickEvent, String unit) {
        this.context = context;
        this.list = list;
        this.horizontalListItemClickEvent = horizontalListItemClickEvent;
        this.unit = unit;
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
        wheelView = (WheelView) dialog.findViewById(R.id.wheelview_weight);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        wheelView.setSkin(WheelView.Skin.Holo);
        wheelView.setWheelData(list);
        wheelView.setStyle(style);
        wheelView.setExtraText(unit, Color.parseColor("#ffffff"), 50, 120);
        wheelView.setLoop(false);
        wheelView.setWheelClickable(true);
        wheelView.setWheelSize(27);
        wheelView.setSelection(30);
        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                weelWeightScrollvalue = o.toString();
            }
        });
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                weelWeightScrollvalue = o.toString();
            }
        });
        wheelWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalListItemClickEvent.onHorizontalListItemClick(weelWeightScrollvalue);
                dismiss();
            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Toast.makeText(getActivity(),"Select at least one item",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }
}

