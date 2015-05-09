package com.appchance.hapticmarkers.models;

public class MarkedArea {

    private int startY;
    private int endY;

    public MarkedArea(int startY, int endY) {
        this.startY = startY;
        this.endY = endY;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndY() {
        return endY;
    }

    @Override
    public String toString() {
        return "MarkedArea{" +
                "startY=" + startY +
                ", endY=" + endY +
                '}';
    }
}
