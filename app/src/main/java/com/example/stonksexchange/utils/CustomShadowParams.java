package com.example.stonksexchange.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CustomShadowParams implements Parcelable {
    private final String name;
    private final List<Shadow> layers;

    public CustomShadowParams(String name, List<Shadow> layers) {
        this.name = name;
        this.layers = layers;
    }

    protected CustomShadowParams(Parcel in) {
        name = in.readString();
        layers = in.createTypedArrayList(Shadow.CREATOR);
    }

    public static final Creator<CustomShadowParams> CREATOR = new Creator<CustomShadowParams>() {
        @Override
        public CustomShadowParams createFromParcel(Parcel in) {
            return new CustomShadowParams(in);
        }

        @Override
        public CustomShadowParams[] newArray(int size) {
            return new CustomShadowParams[size];
        }
    };

    public String getName() {
        return name;
    }

    public List<Shadow> getLayers() {
        return layers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(layers);
    }
}
