package com.example.stonksexchange.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ShadowView extends View {
    private final List<Shadow> shadowLayers = new ArrayList<>();
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float roundRadius = 0f;

    public ShadowView(Context context) {
        super(context);
        init();
    }

    private void init() {
        shadowLayers.add(new Shadow(0, 2, 9, Color.BLACK, 0.14f));
    }

    public void setRoundRadius(float roundRadius) {
        this.roundRadius = roundRadius;
        invalidate(); // Redraw the view when the round radius is updated
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Enable software rendering for the view
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        for (Shadow shadow : shadowLayers) {
            Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            shadowPaint.setARGB(128, 255, 255, 255);

            canvas.drawRoundRect(
                    0f,
                    0f,
                    getWidth(),
                    getHeight(),
                    roundRadius,
                    roundRadius,
                    shadowPaint
            );
        }

        final RectF rectF = new RectF();
        final Paint paint = new Paint();
        paint.setARGB(128, 255, 0, 0);

        rectF.set(80,100, 50, 500);

        canvas.drawRect(rectF, paint);

        canvas.drawRoundRect(
                0f,
                0f,
                getWidth(),
                getHeight(),
                roundRadius,
                roundRadius,
                backgroundPaint
        );
    }
}

