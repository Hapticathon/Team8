package com.appchance.hapticmarkers;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

public class App extends Application {

    public static Vibrator vibrator;
    public static final boolean TPAD = false;
    public static boolean isVibrateOn;

    @Override
    public void onCreate() {
        super.onCreate();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
    }

    public static void vibratePattern(long[] pattern) {
        vibrator.vibrate(pattern, -1);
    }

    public static void vibrateOn() {
        vibrator.vibrate(10 * 1000);
        isVibrateOn = true;
    }

    public static void vibrateOff() {
        vibrator.cancel();
        isVibrateOn = false;
    }

    public static boolean isIsVibrateOn(){
        return isVibrateOn;
    }

}
