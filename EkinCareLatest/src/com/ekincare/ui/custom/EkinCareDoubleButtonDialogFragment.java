package com.ekincare.ui.custom;

import com.ekincare.util.IDialogClickListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


/**
 * Created by jhansi on 03/01/15.
 */
public class EkinCareDoubleButtonDialogFragment extends DialogFragment {
    protected int positiveButtonTitle;
    protected int negativeButtonTitle;
    protected String message;
    protected String title;
    protected boolean isCancelable;
    protected IDialogClickListener dialogListener;

    public EkinCareDoubleButtonDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.positiveButtonTitle = bundle.getInt("positiveButtonTitle");
        this.negativeButtonTitle = bundle.getInt("negativeButtonTitle");
        this.message = bundle.getString("message");
        this.title = bundle.getString("title");
        this.isCancelable = bundle.getBoolean("isCancelable");

    }

    public static EkinCareDoubleButtonDialogFragment newInstance(int positive ,int negative,String message,String alert){
        EkinCareDoubleButtonDialogFragment fragment = new EkinCareDoubleButtonDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("positiveButtonTitle",positive);
        bundle.putInt("negativeButtonTitle",negative);
        bundle.putString("message",message);
        bundle.putString("title",alert);
        bundle.putBoolean("isCancelable",true);
        fragment.setArguments(bundle);

        return fragment;  // you were returning null
    }
    public void setDialogListener(IDialogClickListener dialogListener) {
        this.dialogListener = dialogListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(title)
                .setCancelable(isCancelable)
                .setMessage(message)
                .setPositiveButton(positiveButtonTitle,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (dialogListener != null) {
                                    dialogListener.onPositiveButtonClick(dialog, which);
                                }
                            }
                        }).setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogListener != null) {
                            dialogListener.onNegativeButtonClick(dialog, which);
                        }
                    }
                });

        return builder.create();
    }

}
