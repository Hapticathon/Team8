package com.appchance.hapticmarkers.ui.fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appchance.hapticmarkers.App;
import com.appchance.hapticmarkers.R;
import com.appchance.hapticmarkers.enums.MarkerType;
import com.appchance.hapticmarkers.models.MarkedArea;
import com.appchance.hapticmarkers.models.Marker;
import com.appchance.hapticmarkers.utils.MarkerUtil;
import com.appchance.hapticmarkers.utils.ViewUtil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OverviewFragment extends Fragment {

    @InjectView(R.id.text)
    TextView text;

    private ArrayList<Marker> markers = new ArrayList<>();
    private List<MarkedArea> markedAreas = null;
    private String data = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        AssetManager assetManager = getActivity().getAssets();
        InputStream is = null;
        try {
            is = assetManager.open("text.html");
            data = IOUtils.toString(is);
        } catch (IOException e) {

        }

        text.setText(data);
        text.setOnTouchListener(new OnMarkerTouchListener(getActivity(), new OnTapListener() {
            @Override
            public void onTapListener(int y) {

                int index = MarkerUtil.getMarkerIndex(markedAreas, y);

                if (index != -1) {

                    ReaderFragment fragment = ReaderFragment.getInstance(data, markers, index);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                }

            }
        }));

        markers.add(new Marker(MarkerType.GREEN, 340, 480));
        markers.add(new Marker(MarkerType.RED, 1280, 1450));

        addMarkers(markers);

    }

    private void addMarkers(List<Marker> markers) {

        Spannable spannable = null;

        if (text.getText() instanceof Spannable) {
            spannable = (Spannable) text.getText();
        } else {
            spannable = Spannable.Factory.getInstance().newSpannable(text.getText());
        }

        for (Marker marker : markers) {

            MarkerType type = marker.getType();
            int ss = marker.getSelectionStart();
            int se = marker.getSelectionEnd();

            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(type.getBackgroundColor());
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(type.getForegroundColor());
            spannable.setSpan(backgroundColorSpan, ss, se, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(foregroundColorSpan, ss, se, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        }

        text.setText(spannable);

    }

    public interface OnTapListener {
        void onTapListener(int y);
    }

    public class OnMarkerTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;
        private OnTapListener onTapListener;

        public OnMarkerTouchListener(Context context, OnTapListener onTapListener) {
            this.gestureDetector = new GestureDetector(context, new GestureListener());
            this.onTapListener = onTapListener;
        }

        public void onTap(MotionEvent event) {
            onTapListener.onTapListener((int) event.getY());
        }

        public boolean onTouch(View v, MotionEvent event) {

            if (markedAreas == null) {
                Bitmap bitmap = ViewUtil.getViewBitmap(text);
                markedAreas = MarkerUtil.getMarkedAreasFromBitmap(bitmap);
                Log.d("HM", "markedAreas: " + markedAreas.toString());
            }

            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }

            int action = event.getAction();

            int y = (int) event.getY();

            switch (action) {

                case MotionEvent.ACTION_DOWN:

                    break;

                case MotionEvent.ACTION_MOVE:

                    if (MarkerUtil.isInMarkedArea(markedAreas, y, 15)) {
                        App.vibrateOn();
                    } else {
                        App.vibrateOff();
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    App.vibrateOff();
                    break;
            }

            return true;
        }



        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                onTap(e);
                return true;
            }
        }

    }

}
