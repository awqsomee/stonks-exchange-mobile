package com.example.stonksexchange.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import java.lang.reflect.Field;

public class TypefaceUtil {
    public static final String LOG_TAG1 = "Set font";
    public static final String LOG_TAG2 = "Don`t set font ";
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
            Log.i(LOG_TAG1, "");
        } catch (Exception e) {
            Log.i(LOG_TAG2, "");
        }
    }
}