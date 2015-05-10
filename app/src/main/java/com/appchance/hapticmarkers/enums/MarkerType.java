package com.appchance.hapticmarkers.enums;


import android.graphics.Color;

import com.appchance.hapticmarkers.R;

public enum MarkerType {

    GREEN(Color.parseColor("#FFFFFF"), Color.parseColor("#4CAF50"), new long[] { 0, 100 }, R.mipmap.ic_marker_green),
    RED(Color.parseColor("#FFFFFF"), Color.parseColor("#F44336"), new long[] { 0, 100, 100, 100  }, R.mipmap.ic_marker_red),
    BLUE(Color.parseColor("#FFFFFF"), Color.parseColor("#2196F3"), new long[] { 0, 100, 100, 100, 100, 100 }, R.mipmap.ic_marker_blue);

    int foregroundColor;
    int backgroundColor;
    long[] pattern;
    int markerResource;

    MarkerType(int foregroundColor, int backgroundColor, long[] pattern, int markerResource) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.pattern = pattern;
        this.markerResource = markerResource;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public long[] getPattern() {
        return pattern;
    }

    public int getMarkerResource() {
        return markerResource;
    }
}
