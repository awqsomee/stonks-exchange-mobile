package com.example.stonksexchange.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static String getPathFromUri(Context context, Uri uri) {
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // ContentResolver implementation
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                String fileName = cursor.getString(columnIndex);

                // Create a temporary file to copy the content of the Uri
                File tempFile = new File(context.getCacheDir(), fileName);
                try (InputStream inputStream = contentResolver.openInputStream(uri);
                     OutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[4 * 1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();

                    filePath = tempFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } else {
            if (uri.getScheme().equals("content")) {
                Cursor cursor = null;
                try {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        filePath = cursor.getString(columnIndex);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (uri.getScheme().equals("file")) {
                filePath = uri.getPath();
            }
        }
        return filePath;
    }
}

