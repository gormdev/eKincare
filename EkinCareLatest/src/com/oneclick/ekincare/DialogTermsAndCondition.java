package com.oneclick.ekincare;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ekincare.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class DialogTermsAndCondition extends DialogFragment
{
	private WebView mWebView;
	private static final String TAG = "Agreement ";
	ProgressBar progressDialog;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		Window window = getDialog().getWindow();

		ViewGroup.LayoutParams attributes = window.getAttributes();
		window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288ce")));
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View dialog = inflater.inflate(R.layout.dialog_terms_and_condition, container);

		mWebView = (WebView) dialog.findViewById(R.id.webView1);
		progressDialog = (ProgressBar) dialog.findViewById(R.id.progress_bar);
		progressDialog.setVisibility(View.VISIBLE);

		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		mWebView.loadUrl("https://www.ekincare.com/about/terms");

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i(TAG, "Finished loading URL: " + url);
				progressDialog.setVisibility(View.GONE);

			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Log.e(TAG, "Error: " + description);
			}
		});

		getDialog().setCanceledOnTouchOutside(false);
		getDialog().setCancelable(true);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return dialog;
	}

}