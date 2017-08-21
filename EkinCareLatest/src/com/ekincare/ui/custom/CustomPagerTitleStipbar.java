package com.ekincare.ui.custom;


import com.ekincare.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerTitleStrip;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CustomPagerTitleStipbar extends PagerTitleStrip {
	Typeface typeface;
	Context mContext;
	public CustomPagerTitleStipbar(Context context) {
		super(context);
		mContext = context;
	}
	public CustomPagerTitleStipbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	@SuppressWarnings("deprecation")
	@SuppressLint({ "DrawAllocation", "NewApi" })
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");

		//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, LayoutParams.WRAP_CONTENT);
		for (int i=0; i<this.getChildCount(); i++) {
			if (this.getChildAt(i) instanceof TextView) {
				TextView mTextView = (TextView)this.getChildAt(i);
				//				mTextView.setLayoutParams(layoutParams);
				if(!mTextView.getText().toString().equalsIgnoreCase("")){
					if(i == 0 || i == 2){
						mTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_shadow_effect_white));
						//mTextView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
						mTextView.setTextColor(mContext.getResources().getColor(android.R.color.black));
						//mTextView.setBackgroundColor(Color.parseColor("#4169E1"));
						//mTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
					}else {

					/*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ){
						mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.action_button_blue));
						//mTextView.setBackground(getResources().getDrawable(R.drawable.border_gray));
					}else{
						mTextView.setBackgroundColor(Color.parseColor("#4169E1"));
						//mTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_gray));
					}*/
						mTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_shadow_effect_blue));
					mTextView.setTextColor(getResources().getColor(android.R.color.white));
					}
//					mTextView.setTextSize(getResources().getInteger(R.integer.int_10));
					mTextView.setMinimumWidth(getResources().getInteger(R.integer.int_500));
					mTextView.setGravity(Gravity.CENTER);
//					mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
//					mTextView.setGravity(Gravity.CENTER_VERTICAL);
//					mTextView.setMinHeight(getResources().getInteger(R.integer.int_200));
					mTextView.setMaxWidth(getResources().getInteger(R.integer.int_500));
					/*mTextView.setPadding((int)getResources().getDimension(R.dimen.margin_18), 
							(int)getResources().getDimension(R.dimen.margin_10), 
							(int)getResources().getDimension(R.dimen.margin_10), 
							(int)getResources().getDimension(R.dimen.margin_18));*/
					mTextView.setPadding((int)getResources().getDimension(R.dimen.margin_10),
							(int)getResources().getDimension(R.dimen.margin_10),
							(int)getResources().getDimension(R.dimen.margin_10),
							(int)getResources().getDimension(R.dimen.margin_10));
					mTextView.setSingleLine();
					mTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
					mTextView.setTypeface(typeface);
					//mTextView.setBackgroundColor(Color.parseColor("#FF0000"));

				}else{
//					mTextView.setBackgroundColor(Color.TRANSPARENT);
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ){
						mTextView.setBackground(null);
					}else{
						mTextView.setBackgroundDrawable(null);
					}

				}
			}
		}
	}
}