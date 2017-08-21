package com.ekincare.util;

import android.content.DialogInterface;


public interface IDialogClickListener {

    void onPositiveButtonClick(DialogInterface dialog, int which);

    void onNegativeButtonClick(DialogInterface dialog, int which);
}