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

@SuppressLint("ValidFragment")
public class DialogHeightPicker extends DialogFragment
{
	private Context context;
	private WheelView wheelViewInch,wheelViewFeet;
	ArrayList<String> listFeet,listInch;
	DialogHeightInterface dialogHeightInterface;
	FloatingActionButton weelHeightFloat;
	int weelHightFeetPosition,weelHightInchPosition;
	String weelHightFeetPositionValue,weelHightInchPositionValue;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dialogHeightInterface = (DialogHeightInterface) activity;

	}

	@SuppressLint("ValidFragment")
	public DialogHeightPicker(Context context, ArrayList<String> listFeet, ArrayList<String> listInch)
	{
		this.context = context;
		this.listFeet=listFeet;
		this.listInch=listInch;
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
		wheelViewFeet = (WheelView) dialog.findViewById(R.id.wheelview_feet);
		wheelViewFeet.setWheelAdapter(new ArrayWheelAdapter(context));
		wheelViewFeet.setSkin(WheelView.Skin.Holo);
		wheelViewFeet.setWheelData(listFeet);
		wheelViewFeet.setStyle(style);
		wheelViewFeet.setExtraText("feet", Color.parseColor("#ffffff"), 50, 90);
		wheelViewFeet.setLoop(false);
		wheelViewFeet.setWheelClickable(true);
		wheelViewFeet.setWheelSize(27);
		wheelViewFeet.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
			@Override
			public void onItemClick(int position, Object o) {
				dialogHeightInterface.setFeet(o.toString(),position);
				dismiss();
			}
		});
		wheelViewFeet.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
			@Override
			public void onItemSelected(int position, Object o) {
				weelHightFeetPosition=position;
				weelHightFeetPositionValue=o.toString();
				dialogHeightInterface.setFeet(o.toString(),position);
			}
		});

		wheelViewInch = (WheelView) dialog.findViewById(R.id.wheelview_inch);
		wheelViewInch.setWheelAdapter(new ArrayWheelAdapter(context));
		wheelViewInch.setSkin(WheelView.Skin.Holo);
		wheelViewInch.setWheelData(listInch);
		wheelViewInch.setStyle(style);
		wheelViewInch.setExtraText("inch", Color.parseColor("#ffffff"), 50, 90);
		wheelViewInch.setLoop(false);
		wheelViewInch.setWheelClickable(true);
		wheelViewInch.setWheelSize(27);
		wheelViewInch.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
			@Override
			public void onItemClick(int position, Object o) {
				dialogHeightInterface.setInch(o.toString(),position);
				dismiss();
			}
		});
		wheelViewInch.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
			@Override
			public void onItemSelected(int position, Object o) {
				weelHightInchPosition=position;
				weelHightInchPositionValue=o.toString();
				dialogHeightInterface.setInch(o.toString(),position);
			}
		});

		weelHeightFloat.setVisibility(View.VISIBLE);
		weelHeightFloat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogHeightInterface.setInch(weelHightInchPositionValue,weelHightInchPosition);
				dialogHeightInterface.setFeet(weelHightFeetPositionValue,weelHightFeetPosition);
				dismiss();
			}
		});

		getDialog().setCanceledOnTouchOutside(false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return dialog;
	}

}