package com.message.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class IncomingHraFamilyHistoryCardViewHolder extends MessageViewHolder {
    public ImageView avatar;
    public ViewGroup avatarContainer;

    public TextView textViewDone;
    public CheckBox checkBoxHyperTension,checkBoxCardiac,checkBoxNone, checkBoxCancer,checkBoxDiabetes,checkBoxBP;

    public IncomingHraFamilyHistoryCardViewHolder(View itemView) {
        super(itemView);

        System.out.println("MessageExternalUserTextViewHolder.MessageExternalUserTextViewHolder");
        textViewDone = (TextView) itemView.findViewById(R.id.doneButton);

        avatar = (ImageView) itemView.findViewById(R.id.image_view_avatar);
        avatarContainer = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);

        checkBoxHyperTension = (CheckBox) itemView.findViewById(R.id.hypertension_checkboxFather);
        checkBoxCardiac = (CheckBox) itemView.findViewById(R.id.cardio_disease_checkboxFather);
        checkBoxNone = (CheckBox) itemView.findViewById(R.id.none_checkboxFather);
        checkBoxCancer = (CheckBox) itemView.findViewById(R.id.cancer_checkboxFather);
        checkBoxDiabetes = (CheckBox) itemView.findViewById(R.id.diabetes_checkboxFather);
        checkBoxBP = (CheckBox) itemView.findViewById(R.id.bloodpressure_checkboxFather);
    }
}
