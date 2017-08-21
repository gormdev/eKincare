package com.ekincare.app;

import android.os.Environment;

public class AppConstants {
    public final static String REPORT_URL = "ReportUrl";
    public final static String REPORT_TITLE = "ReportTitle";
    public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/eKincare/Images";
    public final static String PDF_PATH = Environment.getExternalStorageDirectory().getPath() + "/eKincare/PDF";
    public final static String LOCALPATH = "local_path";
}