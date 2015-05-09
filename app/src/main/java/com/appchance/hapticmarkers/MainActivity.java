package com.appchance.hapticmarkers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.appchance.hapticmarkers.ui.fragments.DocumentsFragment;

import butterknife.ButterKnife;
import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.consts.TPadVibration;

public class MainActivity extends AppCompatActivity {

    private TPadImpl tpad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DocumentsFragment()).commit();

        if (App.TPAD) {
            tpad = new TPadImpl(this);
        }

    }

    @Override
    protected void onDestroy() {
        if (App.TPAD) {
            tpad.disconnectTPad();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void vibrateOn() {
        tpad.sendVibration(TPadVibration.SQUARE, 200, 1);
//        tpad.sendFriction(1f);
    }

    public void vibrateOn(float amp) {
        tpad.sendVibration(TPadVibration.SQUARE, 200, amp);
//        tpad.sendFriction(1f);
    }

    public void vibrateOff() {
        tpad.turnOff();
    }

}
