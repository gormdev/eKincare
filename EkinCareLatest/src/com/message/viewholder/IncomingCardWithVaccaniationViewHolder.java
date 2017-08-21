package com.message.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ekincare.R;

/**
 * Created by RaviTejaN on 08-12-2016.
 */

public class IncomingCardWithVaccaniationViewHolder extends MessageViewHolder {

    public TextView vaccaniationName, vaccaniationTime,vaccaniationOutOf,textViewHeader,viewmoreButton;
    public ImageView vaccnationInfo;
    public ImageView vacc1,vacc2,vacc3,vacc4,vacc5,vacc6;
    public ImageView imgLable1,imgLable2,imgLable3,imgLable4,imgLable5;
    public IncomingCardWithVaccaniationViewHolder(View itemView) {
        super(itemView);
        vaccaniationName = (TextView) itemView.findViewById(R.id.fragment_immunization_text_name);
        vaccnationInfo=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_info);
        vaccaniationTime = (TextView) itemView.findViewById(R.id.fragment_immunization_text_time);
        vaccaniationOutOf = (TextView) itemView.findViewById(R.id.fragment_immunization_text_outof);
        vacc1=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_1);
        vacc2=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_2);
        vacc3=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_3);
        vacc4=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_4);
        vacc5=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_5);
        vacc6=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_6);
        imgLable1=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_1lable);
        imgLable2=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_2lable);
        imgLable3=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_3lable);
        imgLable4=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_4lable);
        imgLable5=(ImageView)itemView.findViewById(R.id.fragment_immunization_img_update_5lable);
        viewmoreButton=(TextView)itemView.findViewById(R.id.textview_button1);


    }
}
