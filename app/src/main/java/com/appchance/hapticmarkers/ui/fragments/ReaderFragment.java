package com.appchance.hapticmarkers.ui.fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReaderFragment extends Fragment {

    private static final String FULL_TEXT_ARG = "full_text_arg";
    private static final String MARKER_LIST_ARG = "marker_list_arg";
    private static final String SELECTED_MARKER_ARG = "selected_marker_arg";

    private static final int CHANGE_PAGE_MARGIN = 150;
    private static final int TEXT_OFFSET_CHARACTERS = 100;

    private List<MarkedArea> markedAreas = null;
    private List<Marker> markerList;
    private String fullText;
    private String cropText;
    private int startTextPosition, endTextPosition;
    private int selectedMarker;

    @InjectView(R.id.tv_full_text) TextView fullTextView;

    public static ReaderFragment getInstance(String fullText, ArrayList<Marker> markerList, int selectedMarker){
        Bundle bundle = new Bundle();
        bundle.putString(FULL_TEXT_ARG, fullText);
        bundle.putParcelableArrayList(MARKER_LIST_ARG, markerList);
        bundle.putInt(SELECTED_MARKER_ARG, selectedMarker);
        ReaderFragment fragment = new ReaderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, container, false);
        ButterKnife.inject(this, view);
        selectedMarker = getArguments().getInt(SELECTED_MARKER_ARG);
        markerList = getArguments().getParcelableArrayList(MARKER_LIST_ARG);
        fullText = getArguments().getString(FULL_TEXT_ARG);
        startTextPosition = Math.max(markerList.get(selectedMarker).getSelectionStart() - TEXT_OFFSET_CHARACTERS, selectedMarker);
        endTextPosition = Math.min(markerList.get(selectedMarker).getSelectionEnd() + TEXT_OFFSET_CHARACTERS, fullText.length()-1);

        cropText = fullText.substring(startTextPosition, endTextPosition);
        fullTextView.setText(cropText);
        initText();
        return view;
    }

    private void initText() {
        Spannable spannable = null;

        if (fullTextView.getText() instanceof Spannable) {
            spannable = (Spannable) fullTextView.getText();
        } else {
            spannable = Spannable.Factory.getInstance().newSpannable(fullTextView.getText());
        }

        for(Marker marker : markerList) {
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(marker.getType().getBackgroundColor());
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(marker.getType().getForegroundColor());
                spannable.setSpan(backgroundColorSpan, marker.getSelectionStart(), marker.getSelectionEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(foregroundColorSpan, marker.getSelectionStart(), marker.getSelectionEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        fullTextView.setText(spannable);

    }

    private int getStartSelection(Marker marker){
        return 0;
    }

    private int getEndSelection(Marker marker){
        return 0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fullTextView.setOnTouchListener(new OnMarkerTouchListener(getActivity(), new OnSwipeListener() {
            @Override
            public void onSwipeLeft(int y) {
                int index = MarkerUtil.getMarkerIndex(markedAreas, y);

                if (index != -1) {
                    MarkerType markerType = markerList.get(index).getType();
                    App.vibratePattern(markerType.getPattern());
                }

            }

            @Override
            public void onSwipeRight(int y) {
                App.vibrateOff();
            }
        }));

    }

    public interface OnSwipeListener {

        void onSwipeLeft(int y);

        void onSwipeRight(int y);
    }

    public class OnMarkerTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;
        private OnSwipeListener onSwipeListener;

        public OnMarkerTouchListener(Context context, OnSwipeListener onSwipeListener) {
            this.gestureDetector = new GestureDetector(context, new GestureListener());
            this.onSwipeListener = onSwipeListener;
        }

        public void onSwipeLeft(int y) {
            onSwipeListener.onSwipeLeft(y);
        }

        public void onSwipeRight(int y) {
            onSwipeListener.onSwipeRight(y);
        }

        public boolean onTouch(View v, MotionEvent event) {

            if (markedAreas == null) {
                Bitmap bitmap = ViewUtil.getViewBitmap(fullTextView);
                markedAreas = MarkerUtil.getMarkedAreasFromBitmap(bitmap);
                Log.d("HM", "markedAreas: " + markedAreas.toString());
            }

            if(gestureDetector.onTouchEvent(event)) {
                return true;
            }

            int action = event.getAction();

            int y = (int) event.getY();

            switch (action) {

                case MotionEvent.ACTION_DOWN:

                    break;

                case MotionEvent.ACTION_MOVE:

                    if (event.getY() < CHANGE_PAGE_MARGIN) {
                        //PREVIOUS PAGE
                        App.vibrateOn();
                    } else if (event.getY() > fullTextView.getHeight() - CHANGE_PAGE_MARGIN) {
                        //NEXT PAGE
                        App.vibrateOn();
                    } else {
                        App.vibrateOff();
                    }

                    if (MarkerUtil.isInMarkedArea(markedAreas, y, 10)) {
                        App.vibrateOn();
                    } else {
                        App.vibrateOff();
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    App.vibrateOff();
                    break;
            }

            return false;
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) {
                        onSwipeRight((int) e1.getY());
                    } else {
                        onSwipeLeft((int) e1.getY());
                    }
                    return true;
                }
                return false;
            }
        }
    }

}
