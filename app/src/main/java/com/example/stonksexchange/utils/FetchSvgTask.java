package com.example.stonksexchange.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.caverock.androidsvg.SVG;
import com.example.stonksexchange.R;

import java.net.URL;

class FetchSvgTask extends AsyncTask<Void, Void, SVG> {

    private String svgUrl;
    private ImageView imageView;
    private Context context;

    public FetchSvgTask(String svgUrl, ImageView imageView, Context context) {
        this.svgUrl = svgUrl;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected SVG doInBackground(Void... params) {
        try {
            SVG svg = SVG.getFromInputStream(new URL(svgUrl).openStream());
            return svg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(SVG svg) {
        if (svg != null) {
            Picture picture = svg.renderToPicture();
            PictureDrawable pictureDrawable = new PictureDrawable(picture);

            Bitmap pictureBitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(),
                    pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(pictureBitmap);
            pictureDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            pictureDrawable.draw(canvas);

            Resources resources = context.getResources();
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(resources, pictureBitmap);
            roundedDrawable.setCornerRadius(8); // Set the desired corner radius

// Set the rounded drawable as the background of the ImageView
            imageView.setImageDrawable(roundedDrawable);
        }
    }
}
