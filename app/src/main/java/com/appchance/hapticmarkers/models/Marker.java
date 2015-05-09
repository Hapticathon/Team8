package com.appchance.hapticmarkers.models;

import com.appchance.hapticmarkers.enums.MarkerType;

public class Marker {

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
}
