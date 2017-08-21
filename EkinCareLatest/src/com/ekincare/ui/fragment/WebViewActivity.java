package com.ekincare.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ekincare.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.LoginActivity;

/**
 * Created by Ajay on 14-07-2016.
 */
public class WebViewActivity extends Activity {

    private WebView mWebView;
    private static final String TAG = "Agreement ";
    private Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_terms);

        mWebView = (WebView) findViewById(R.id.webView1);
      /*  webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.ekincare.com/about/terms");
*/
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        showPDialog();
        mWebView.loadUrl("https://www.ekincare.com/about/terms");


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                hidePDialog();

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
            }
        });


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
        mAlert_Dialog.setCancelable(false);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
