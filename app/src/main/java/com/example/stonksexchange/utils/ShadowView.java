package com.example.stonksexchange.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class ShadowView extends View {
    private final Paint shadowPaint;
    private final Paint backgroundPaint;

    public ShadowView(Context context) {
        super(context);
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_SOFTWARE, null); // Enable software rendering for the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Adjust the dimensions to accommodate the shadow
        float shadowOffsetX = 10f;
        float shadowOffsetY = 10f;
        float shadowSize = 20f;
        RectF shadowRect = new RectF(
                shadowOffsetX,
                shadowOffsetY,
                width - shadowOffsetX,
                height - shadowOffsetY
        );

        shadowPaint.setShadowLayer(shadowSize, 0f, 0f, Color.BLACK);
        backgroundPaint.setColor(Color.WHITE);

        canvas.drawRoundRect(shadowRect, 20f, 20f, shadowPaint);
        canvas.drawRoundRect(shadowRect, 20f, 20f, backgroundPaint);
    }
}



