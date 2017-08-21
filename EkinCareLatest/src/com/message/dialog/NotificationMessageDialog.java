package com.message.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ekincare.R;
import com.message.custominterface.CardWithListButtonClickEvent;
import com.message.custominterface.NotificationDialogClickEvent;


/**
 * Created by Ajay on 25-11-2016.
 */

@SuppressLint("ValidFragment")
public class NotificationMessageDialog extends DialogFragment {

    Context context;
    String title,subTitle,button1,button1Action,button2,button2Action;
    private NotificationDialogClickEvent notificationdialogclickevent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public NotificationMessageDialog(Context context, NotificationDialogClickEvent notificationdialogclickevent, String title, String subTitle, String button1, String button1Action, String button2, String button2Action){
        this.context=context;
        this.notificationdialogclickevent=notificationdialogclickevent;
        this.subTitle =subTitle;
        this.title=title;
        this.button1=button1;
        this.button1Action=button1Action;
        this.button2=button2;
        this.button2Action=button2Action;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View dialog = inflater.inflate(R.layout.item_message_dialog, container);

        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textview_title);
        TextView textViewDiscription= (TextView) dialog.findViewById(R.id.textview_discription);

        final TextView textViewButton1 = (TextView) dialog.findViewById(R.id.button1);
        TextView textViewButton2 = (TextView) dialog.findViewById(R.id.button2);

        System.out.println("NotificationMessageDialog.onCreateView button2="+button2 + " button1="+button1);

        textViewTitle.setText(title);
        textViewDiscription.setText(subTitle);
        textViewButton1.setText(button1);
        textViewButton2.setText(button2);
        textViewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    notificationdialogclickevent.onNotificationClick(button1Action);
                    dismiss();
            }
        });

        textViewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationdialogclickevent.onNotificationClick(button2Action);
                dismiss();
            }
        });

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }
}
