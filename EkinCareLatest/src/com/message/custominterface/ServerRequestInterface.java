package com.message.custominterface;

import com.message.model.PackageItem;
import com.message.model.WalletResponse;

import org.json.JSONObject;

/**
 * Created by Ajay on 24-11-2016.
 */

public interface ServerRequestInterface {
    public void onChatServeResponse(JSONObject response,String textMessage);

    public void onAddFamilyResponse(JSONObject response);

    public void onDoctorSpecializationResponse(JSONObject response,String noJson);

    public void onHraComplitionResponse(JSONObject response,String idToken);

    public void onDocumentUploadResponse(String text,boolean isUploaded);

    public void onPictureUploadResponse(JSONObject response);

    public void onErrorResponse(String error,String textMessage);

    public void onUpdateAppResponse();

    public void onLocationResponse(String address);

    public void onGetPackageResponse(JSONObject response,String noJson);

    public void onGetPackageFeeResponse(JSONObject response,String noJson);

    public void onDCPackagesResponse(JSONObject response);

    public void onProfileResponse(JSONObject response);



    public void onSpecializationResponse(JSONObject response,String noJson);

    public void onPackageDoctorList(JSONObject response,String noJson);

    public void onPackageNameData(PackageItem packageItem, boolean isLast, boolean notFound, String part, int size);

    public void onPaymentSuccessServerRequest(JSONObject result);

    public void onWalletResponse(WalletResponse walletResponse);

    public void onWalletResponseFirst(WalletResponse walletResponse);

    public void onNotificationResponseFirst(JSONObject response);

    public void profileUpdated();
}
