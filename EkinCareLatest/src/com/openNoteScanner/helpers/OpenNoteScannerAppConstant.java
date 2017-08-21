package com.openNoteScanner.helpers;

import android.os.Environment;

import java.util.Arrays;
import java.util.List;

public class OpenNoteScannerAppConstant {

    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
            "png");

    public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/scanSample";

    public final static String SCANNED_RESULT_ALL = "scannedResultAll";

    public final static String UPLOAD_DOC = "UPLOAD_DOC";
}
