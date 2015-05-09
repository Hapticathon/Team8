package com.appchance.hapticmarkers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.appchance.hapticmarkers.enums.MarkerType;

public class Marker implements Parcelable {

    private MarkerType type;
    private int selectionStart;
    private int selectionEnd;

    public Marker(MarkerType type, int selectionStart, int selectionEnd) {
        this.type = type;
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }

    public MarkerType getType() {
        return type;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    protected Marker(Parcel in) {
        type = (MarkerType) in.readValue(MarkerType.class.getClassLoader());
        selectionStart = in.readInt();
        selectionEnd = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeInt(selectionStart);
        dest.writeInt(selectionEnd);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Marker> CREATOR = new Parcelable.Creator<Marker>() {
        @Override
        public Marker createFromParcel(Parcel in) {
            return new Marker(in);
        }

        @Override
        public Marker[] newArray(int size) {
            return new Marker[size];
        }
    };
}
