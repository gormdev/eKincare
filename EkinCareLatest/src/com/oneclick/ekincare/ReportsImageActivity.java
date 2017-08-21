package com.oneclick.ekincare;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.ekincare.R;
import com.ekincare.app.AppConstants;
import com.ekincare.app.CustomerManager;
import com.ekincare.ui.custom.TouchImageView;
import com.ekincare.util.ImageDownloader;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.helper.PreferenceHelper;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;

public class ReportsImageActivity extends AppCompatActivity  {

	protected ImageDownloader imageDownloader;
	private Toolbar toolbar;
	ImageView reportImage;
	TouchImageView reportImageView;
	CircleProgressBar progressWithArrow;
	private Dialog mAlert_Dialog;
	private PreferenceHelper prefs;
	CustomerManager customerManager;
	private String strFamilyMemberKey = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_image);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setupToolbar();
		prefs = new PreferenceHelper(this);
		customerManager = CustomerManager.getInstance(getApplicationContext());

		imageDownloader = new ImageDownloader();

		String url = getIntent().getStringExtra(AppConstants.REPORT_URL);
		String localPath = getIntent().getStringExtra(AppConstants.LOCALPATH);
		reportImageView = (TouchImageView) findViewById(R.id.reportImage);
		reportImage=(ImageView)findViewById(R.id.reportImage2);

		if (url != null) {
			downloadNewIMage(url);
		}else if (localPath != null) {
			System.out.println("Documents======"+"NO"+url);
			File imgFile = new File(localPath);
			if (imgFile.exists()) {
				showPDialog();
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				reportImage.setImageBitmap(myBitmap);
			}
		}

	}

	private void downloadNewIMage(String requestUrl) {
		showPDialog();
		RequestQueue rq = Volley.newRequestQueue(this);
		ImageRequest ir = new ImageRequest(requestUrl, new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						hidePDialog();
						reportImage.setImageBitmap(response);
					}
				}, maxWidth, maxHeight,
				Bitmap.Config.RGB_565, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//onGetImageError(cacheKey, error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				if (customerManager.isLoggedInCustomer()) {
					strFamilyMemberKey = "";
				} else {
					strFamilyMemberKey = customerManager.getCurrentFamilyMember().getIdentification_token();
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
				params.put("X-EKINCARE-KEY", prefs.getEkinKey());
				params.put("X-DEVICE-ID", customerManager.getDeviceID(ReportsImageActivity.this));
				if (!strFamilyMemberKey.equalsIgnoreCase(""))
					params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
				return params;
			}
		};
		rq.add(ir);

	}



	private void hidePDialog() {
		if (mAlert_Dialog != null) {
			mAlert_Dialog.dismiss();
			mAlert_Dialog=null;
		}
	}

	private void showPDialog() {
		mAlert_Dialog = new Dialog(this);
		mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mAlert_Dialog.setContentView(R.layout.materialprogressbar);
		mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//mAlert_Dialog.setCancelable(false);
		mAlert_Dialog.setCanceledOnTouchOutside(false);
		progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
		progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
		mAlert_Dialog.show();
	}

	private void setupToolbar() {
		try{
			setSupportActionBar(toolbar);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			toolbar.setTitle(getIntent().getStringExtra(AppConstants.REPORT_TITLE));

			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ReportsImageActivity.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
					ReportsImageActivity.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
					finish();
				}
			});

		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}



}