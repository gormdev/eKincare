package com.oneclick.ekincare.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.ekincare.app.AppConstants;
import com.ekincare.util.FileType;
import com.ekincare.util.ScalingUtilities;

/**
 * Created by jhansi on 05/01/15.
 */
public class FileUtil {

    protected static List<String> imageTypes = new ArrayList<String>(Arrays.asList(".jpg", ".png", ".jpeg", ".gif"));
    protected static String pdfExtension = ".pdf";

    public static FileType getFileType(String url) {
        String fileExtension = url.substring(url.lastIndexOf("."));
        if (isImageType(fileExtension)) {
            return FileType.image;
        } else if (isPdfType(fileExtension)) {
            return FileType.pdf;
        }
        return FileType.other;
    }

    public static boolean isImageType(String extension) {
        return imageTypes.contains(extension.toLowerCase());
    }

    public static boolean isPdfType(String extension) {
        return pdfExtension.equals(extension.toLowerCase());
    }

    public static String decodeFile(String path, int desiredWidth, int desiredHeight,String fileName) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, desiredWidth, desiredHeight, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= desiredWidth && unscaledBitmap.getHeight() <= desiredHeight)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, desiredWidth, desiredHeight, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }
            String s = path.substring(path.lastIndexOf("/"));
            File f = new File(AppConstants.IMAGE_PATH, s);
            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }

    @SuppressLint("NewApi")
	public static String getPath(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isGoogleDriveDocument(uri)) {

                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    //File file = new File();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                String filePath = null;

                Log.d("", "URI = " + uri);
                if (uri != null && "content".equals(uri.getScheme())) {
                    Cursor cursor = context.getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                    filePath = "/storage/sdcard0/Android/data/com.google.android.apps.docs/" + filePath;
                    cursor.close();
                } else {
                    filePath = uri.getPath();
                }
                Log.d("", "Chosen path = " + filePath);
                return filePath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isGoogleDriveDocument(Uri uri) {
        return uri.getAuthority().contains("com.google.android.apps.docs");
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
