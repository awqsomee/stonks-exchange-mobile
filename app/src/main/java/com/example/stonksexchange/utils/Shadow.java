package com.example.stonksexchange.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Shadow implements Parcelable {
    private final float dX;
    private final float dY;
    private final float radius;
    private final int color;
    private final float colorAlpha;

    public Shadow(float dX, float dY, float radius, int color, float colorAlpha) {
        this.dX = dX;
        this.dY = dY;
        this.radius = radius;
        this.color = color;
        this.colorAlpha = colorAlpha;
    }

    protected Shadow(Parcel in) {
        dX = in.readFloat();
        dY = in.readFloat();
        radius = in.readFloat();
        color = in.readInt();
        colorAlpha = in.readFloat();
    }

    public static final Creator<Shadow> CREATOR = new Creator<Shadow>() {
        @Override
        public Shadow createFromParcel(Parcel in) {
            return new Shadow(in);
        }

        @Override
        public Shadow[] newArray(int size) {
            return new Shadow[size];
        }
    };

    public float getDX() {
        return dX;
    }

    public float getDY() {
        return dY;
    }

    public float getRadius() {
        return radius;
    }

    public int getColor() {
        return color;
    }

    public float getColorAlpha() {
        return colorAlpha;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(dX);
        dest.writeFloat(dY);
        dest.writeFloat(radius);
        dest.writeInt(color);
        dest.writeFloat(colorAlpha);
    }
}
