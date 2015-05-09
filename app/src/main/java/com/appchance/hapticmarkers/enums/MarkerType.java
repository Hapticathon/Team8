package com.appchance.hapticmarkers.enums;


import android.graphics.Color;

public enum MarkerType {

    GREEN(Color.parseColor("#FFFFFF"), Color.parseColor("#4CAF50"), new long[] { 0, 100 }),
    RED(Color.parseColor("#FFFFFF"), Color.parseColor("#F44336"), new long[] { 0, 100, 100, 100  }),
    BLUE(Color.parseColor("#FFFFFF"), Color.parseColor("#2196F3"), new long[] { 0, 100, 100, 100, 100, 100 });

    int foregroundColor;
    int backgroundColor;
    long[] pattern;

    MarkerType(int foregroundColor, int backgroundColor, long[] pattern) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.pattern = pattern;
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
}
