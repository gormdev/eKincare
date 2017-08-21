package com.ekincare.util;

import android.app.ProgressDialog;
import android.content.Context;
/**
 * @author Mayank.Rai@TABS100
 * @version 1.0
 * @since   2016-04-21 
 * This class is responsible for creating dialogs for various occasions.
 *
 */
public class ProgressDialogUtility 
{
	private ProgressDialog pDialog;
	
	public ProgressDialogUtility(Context context)
	{
		// Progress dialog
		pDialog = new ProgressDialog(context);
		pDialog.setCancelable(true);
		pDialog.setCanceledOnTouchOutside(false);
	}
	
	public void startProgressBar(String message)
	{
		pDialog.setMessage(message);
		showDialog();
	}
	
	public void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	public void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
