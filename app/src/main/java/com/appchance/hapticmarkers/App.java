package com.appchance.hapticmarkers;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

public class App extends Application {

    private static Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
    }

    public static void vibrateOn() {
        vibrator.vibrate(10 * 1000);
    }

    public static void vibrateOff() {
        vibrator.cancel();
    }

}
