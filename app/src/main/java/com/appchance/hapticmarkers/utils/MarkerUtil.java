package com.appchance.hapticmarkers.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.appchance.hapticmarkers.models.MarkedArea;

import java.util.ArrayList;
import java.util.List;

public class MarkerUtil {

    public static List<MarkedArea> getMarkedAreasFromBitmap(Bitmap bitmap) {

        List<MarkedArea> markedAreas = new ArrayList<>();

        boolean isMarked = false;
        int startY = 0;

        List<Integer> spannedColors = new ArrayList<>();
        spannedColors.add(Color.parseColor("#009688"));

        for (int y=0; y < bitmap.getHeight(); y++) {

            boolean containsMarker = false;

            for(int x=0; x < bitmap.getWidth(); x++) {

                int color = bitmap.getPixel(x, y);

                if (spannedColors.contains(color)) {
                    containsMarker = true;
                }

            }

            if (containsMarker) {
                if (!isMarked) {
                    isMarked = true;
                    startY = y;
                }
            } else {
                if (isMarked) {
                    isMarked = false;
                    markedAreas.add(new MarkedArea(startY, y));
                }
            }

        }

        return markedAreas;
    }

    public static boolean isInMarkedArea(List<MarkedArea> markedAreas, int y) {

        int margin = 50;

        for (MarkedArea markedArea : markedAreas) {

            int startY = markedArea.getStartY() - margin;
            int endY = markedArea.getEndY() + margin;

            if (startY - margin < y && endY > y) {
                return true;
            }
        }

        return false;
    }

}