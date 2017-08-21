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
import com.oneclick.ekincare.DialogHeightInterface;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class DialogTwoPicker extends DialogFragment
{
	private Context context;
	private WheelView wheelView1, wheelView2;
	ArrayList<String> list1, list2;
	HorizontalListItemClickEvent horizontalListItemClickEvent;
	String unit1, unit2;
	FloatingActionButton weelHeightFloat;
	int weelHightFeetPosition,weelHightInchPosition;
	String weelHightFeetPositionValue,weelHightInchPositionValue;

	@SuppressLint("ValidFragment")
	public DialogTwoPicker(Context context, ArrayList<String> list1, ArrayList<String> list2,
						   HorizontalListItemClickEvent horizontalListItemClickEvent, String unit1, String unit2)
	{
		this.context = context;
		this.list1 =list1;
		this.list2 =list2;
		this.horizontalListItemClickEvent =horizontalListItemClickEvent;
		this.unit1 =unit1;
		this.unit2 =unit2;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
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
		window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288ce")));
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View dialog = inflater.inflate(R.layout.weel_scroll_dialog_height, container);

		WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
		style.selectedTextColor = Color.parseColor("#fcc633");
		style.textColor = Color.parseColor("#F5F5F5");
		style.selectedTextSize = 25;
		style.textSize = 18;
		style.selectedTextZoom = 2.0f;
		style.backgroundColor =  Color.parseColor("#0288ce");
		style.textAlpha = 1;
		style.holoBorderColor = Color.parseColor("#F5F5F5");

		weelHeightFloat=(FloatingActionButton)dialog.findViewById(R.id.weel_height_floating);
		wheelView2 = (WheelView) dialog.findViewById(R.id.wheelview_feet);
		wheelView2.setWheelAdapter(new ArrayWheelAdapter(context));
		wheelView2.setSkin(WheelView.Skin.Holo);
		wheelView2.setWheelData(list1);
		wheelView2.setStyle(style);
		wheelView2.setExtraText(unit1, Color.parseColor("#ffffff"), 50, 90);
		wheelView2.setLoop(false);
		wheelView2.setWheelClickable(true);
		wheelView2.setWheelSize(27);
		wheelView2.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
			@Override
			public void onItemClick(int position, Object o) {
				weelHightFeetPositionValue=o.toString();
				dismiss();
			}
		});
		wheelView2.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
			@Override
			public void onItemSelected(int position, Object o) {
				weelHightFeetPosition=position;
				weelHightFeetPositionValue=o.toString();
			}
		});

		wheelView1 = (WheelView) dialog.findViewById(R.id.wheelview_inch);
		wheelView1.setWheelAdapter(new ArrayWheelAdapter(context));
		wheelView1.setSkin(WheelView.Skin.Holo);
		wheelView1.setWheelData(list2);
		wheelView1.setStyle(style);
		wheelView1.setExtraText(unit2, Color.parseColor("#ffffff"), 50, 90);
		wheelView1.setLoop(false);
		wheelView1.setWheelClickable(true);
		wheelView1.setWheelSize(27);
		wheelView1.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
			@Override
			public void onItemClick(int position, Object o) {
				weelHightInchPosition=position;
				weelHightInchPositionValue=o.toString();
			}
		});
		wheelView1.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
			@Override
			public void onItemSelected(int position, Object o) {
				weelHightInchPosition=position;
				weelHightInchPositionValue=o.toString();
			}
		});

		weelHeightFloat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				horizontalListItemClickEvent.onHorizontalListItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
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