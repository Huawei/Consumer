package com.example.scankitdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

public class MainActivity extends Activity {

    public static final int BITMAP = 0x22;

    public static final int REQUEST_CODE_PHOTO = 0x33;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void newViewBtnClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    BITMAP);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode   ,   String[] permissions, int[] grantResults) {
        if (permissions == null || grantResults == null || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (requestCode == BITMAP) {
            // Call the system album.
            Intent pickIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            MainActivity.this.startActivityForResult(pickIntent, REQUEST_CODE_PHOTO);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //receive result after your activity finished scanning
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_PHOTO) {
            // Obtain the image path.
            final String path = getImagePath(MainActivity.this, data);
            if (TextUtils.isEmpty(path)) {
                return;
            }
            // Obtain the bitmap from the image path.
            Bitmap bitmap = ScanUtil.compressBitmap(MainActivity.this, path);
            // Call the decodeWithBitmap method to pass the bitmap.
			HmsScan[] result1 = ScanUtil.decodeWithBitmap(MainActivity.this, bitmap, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(0).setPhotoMode(true).create());
            // Obtain the scanning result.
			if (result1 != null && result1.length > 0) {
                if (!TextUtils.isEmpty(result1[0].getOriginalValue())) {
                    Toast.makeText(this, result1[0].getOriginalValue(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /** getImagePath form intent
     */
    private static  String getImagePath(Context context, Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        //get api version
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId
                            (Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                    imagePath = getImagePath(context, contentUri, null);
                } else {
                    Log.i(TAG, "getImagePath  uri.getAuthority():" + uri.getAuthority());
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(context, uri, null);
            } else {
                Log.i(TAG, "getImagePath  uri.getScheme():" + uri.getScheme());
            }
        } else {
            imagePath = getImagePath(context, uri, null);
        }
        return imagePath;
    }

    /**
     * get image path from system album by uri
     */
    private static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
