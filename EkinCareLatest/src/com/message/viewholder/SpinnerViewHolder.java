package com.message.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ekincare.R;

/**
 * Created by matthewpage on 7/5/16.
 */
public class SpinnerViewHolder extends MessageViewHolder {
    public ProgressBar spinner;
    public LinearLayout linearLayout;
    public TextView textView;
    public SpinnerViewHolder(View itemViews) {
        super(itemViews);
        this.spinner = (ProgressBar) itemView.findViewById(R.id.loading_bar);
        textView =  (TextView) itemView.findViewById(R.id.text_load_more);
        linearLayout =  (LinearLayout) itemView.findViewById(R.id.spinner_container);
    }
}