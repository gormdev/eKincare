package com.oneclick.ekincare.helper;



import com.ekincare.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class CustomeDialog {




	public static void dispDialog(Activity ac,String dialogMsg){
		final AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		builder.setTitle("");
		builder.setMessage(dialogMsg);

		String positiveText = "Close";
		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		// display dialog
		dialog.show();
	}

	public static void showToast(final Activity ac,String errorMessage){
		Toast.makeText(ac, errorMessage, Toast.LENGTH_SHORT).show();
	}



}
