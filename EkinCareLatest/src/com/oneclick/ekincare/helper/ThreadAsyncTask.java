package com.oneclick.ekincare.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ekincare.R;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ThreadAsyncTask extends AsyncTask<String, Void, Object> {

    // private ProgressDialog mAlert_Dialog;
    private Dialog mAlert_Dialog;
    private String method;
    private Context context;
    private String json;
    private Object mObject;
    private OnTaskCompleted listener;
    private NetworkCaller mNetworkCaller;
    List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    List<Header> Headers = new ArrayList<Header>();
    private String AddArrayname;

    private HttpEntity entity;
    private boolean IsGet = false;

    // ProgressHUD mProgressHUD;

    // private EkinCareProgressDialogFragment progressDialogFragment;

    public interface OnTaskCompleted {
        void onTaskCompleted(String method, Object result);
    }



    /**
     * @param listener
     * @param con
     * @param json
     * @param method
     */
    public ThreadAsyncTask(OnTaskCompleted listener, Context con, Object object, String json, String method,
                           List<NameValuePair> postParameters, String AddArrayName, boolean isGet, List<Header> mHeaders,
                           HttpEntity entity)
    {
        this.listener = listener;
        this.context = con;
        this.json = json;
        this.method = method;
        this.mObject = object;
        this.postParameters = postParameters;
        this.AddArrayname = AddArrayName;
        this.IsGet = isGet;
        this.Headers = mHeaders;
        this.entity = entity;
    }

    /**
     * @param listener
     * @param con
     * @param json
     * @param method
     */
    public ThreadAsyncTask(OnTaskCompleted listener, Context con, Object object, String json, String method,
                           List<NameValuePair> postParameters, String AddArrayName, boolean isGet, List<Header> mHeaders) {
        this.listener = listener;
        this.context = con;
        this.json = json;
        this.method = method;
        this.mObject = object;
        this.postParameters = postParameters;
        this.AddArrayname = AddArrayName;
        this.IsGet = isGet;
        this.Headers = mHeaders;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        if (TextUtils.isEmpty(AddArrayname)) {
            mAlert_Dialog = new Dialog(context);
            mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mAlert_Dialog.setContentView(R.layout.circular_progressbar);

            mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            mAlert_Dialog.setCancelable(true);
            mAlert_Dialog.setCanceledOnTouchOutside(true);
            mAlert_Dialog.show();
        } else if ((!method.equalsIgnoreCase(TagValues.PUSH_TOCKENS_URL)
                && !method.equalsIgnoreCase(TagValues.Get_All_Documents_URL)
                && !method.equalsIgnoreCase(TagValues.GET_CUSTOMER_DETAIL_URL)) || AddArrayname.equalsIgnoreCase("true")) {
            // && !method.equalsIgnoreCase(TagValues.ADD_WIZARD_DETAIL_URL)


            mAlert_Dialog = new Dialog(context);
            mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mAlert_Dialog.setContentView(R.layout.circular_progressbar);
            mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            mAlert_Dialog.setCancelable(false);
            mAlert_Dialog.show();

            // progressDialogFragment = new
            // EkinCareProgressDialogFragment("Please Wait...");
            // FragmentManager fm = con.getSupportFragmentManager();
            // progressDialogFragment.show(fm,
            // EkinCareProgressDialogFragment.class.toString());
        }

        if (method.equalsIgnoreCase(TagValues.GET_CUSTOMER_DETAIL_URL)) {
            AddArrayname = "";
        }
    }

    @Override
    protected Object doInBackground(String... urls) {
        for (String url : urls) {
            if (isCancelled()) {
                break;
            }
            if (method.equalsIgnoreCase(TagValues.SEND_BLOOD_SOS)) {
                mNetworkCaller = new NetworkCaller(context);

                try {
                    mObject = mNetworkCaller.httpPostData(this.method, this.json, this.Headers);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    return new Exception("Sorry Problem while processing the request");
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    return new Exception("Sorry Bad request while processing");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    return new Exception("Unable to Connect to server please try later.");
                }

            } else if (method.equalsIgnoreCase(TagValues.SEND_TO_DOCTOR)) {
                mNetworkCaller = new NetworkCaller(context);

                try {
                    mObject = mNetworkCaller.httpPostData(this.method, this.json, this.Headers);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    return new Exception("Sorry Problem while processing the request");
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    return new Exception("Sorry Bad request while processing");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    return new Exception("Unable to Connect to server please try later.");
                }

                return mObject;
            } else if (method.equalsIgnoreCase(TagValues.GOOGLE_FIT_HISTORY)) {
                mNetworkCaller = new NetworkCaller(context);

				try {
					mObject = mNetworkCaller.httpPostData(this.method, this.json, this.Headers);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					return new Exception("Sorry Problem while processing the request");
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					return new Exception("Sorry Bad request while processing");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return new Exception("Unable to Connect to server please try later.");
				}

				return mObject;
			}else if (method.equalsIgnoreCase(TagValues.LOGIN_URL)) {
                mNetworkCaller = new NetworkCaller(context);
                mObject = mNetworkCaller.httpPostDataLogin(method, mObject, postParameters, json, AddArrayname);
            } else if (method.equalsIgnoreCase(TagValues.UPDATE_GLUCOSE_URL)
                    || method.equalsIgnoreCase(TagValues.UPDATE_BLOOD_PRESSURE_URL)
                    || method.equalsIgnoreCase(TagValues.UPDATE_VITALS_URL)
                    || method.contains(TagValues.UPDATE_IMMUNIZATION_URL)) {
                mNetworkCaller = new NetworkCaller(context);
                mObject = mNetworkCaller.httpPatchData(method, mObject, postParameters, json, AddArrayname, Headers);
            } else {

                if (IsGet) {
                    mNetworkCaller = new NetworkCaller(context);
                    mObject = mNetworkCaller.cachedGet(method, context, mObject, json, AddArrayname, Headers);
                } else {

                    System.out.println("result======= postParameters" + postParameters);
                    if (method.equalsIgnoreCase(TagValues.Get_All_Documents_URL)) {
                        mNetworkCaller = new NetworkCaller(context);
                        mObject = mNetworkCaller.httpPostFile(method, mObject, postParameters, url, AddArrayname, Headers, entity);
                    } else if (method.equalsIgnoreCase(TagValues.UPDATE_PROFILE_PICTURE_URL)) {
                        mNetworkCaller = new NetworkCaller(context);
                        mObject = mNetworkCaller.httpPatchFile(method, mObject, postParameters, url, AddArrayname,
                                Headers, entity);
                    } else {
                        mNetworkCaller = new NetworkCaller(context);
                        mObject = mNetworkCaller.httpPostData(method, mObject, postParameters, json, AddArrayname,
                                Headers);

                    }

                    // mNetworkCaller = new NetworkCaller(con);
                    // mObject =
                    // mNetworkCaller.httpPostData(method,mObject,postParameters,json,AddArrayname,Headers);
                }
            }

        }
        return mObject;


    }


    @Override
    protected void onPostExecute(Object result) {
        // if(progressDialogFragment != null){
        // progressDialogFragment.dismissAllowingStateLoss();
        // }

        if (result != null) {
            Log.e("Response", result.toString());
            System.out.println("result=======" + result.toString());
        }

        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
        }

        if (listener != null)
            listener.onTaskCompleted(method, result);
    }


}
