package com.appchance.hapticmarkers.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appchance.hapticmarkers.App;
import com.appchance.hapticmarkers.MainActivity;
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

    private static final String TITLE_ARG = "title_arg";
    private static final String PATH_ARG = "path_arg";

    @InjectView(R.id.text)
    TextView text;

    @InjectView(R.id.ll_loading)
    LinearLayout loadingLayout;

    @InjectView(R.id.iv_flow_marker)
    ImageView flowMarker;

    private ArrayList<Marker> markers = new ArrayList<>();
    private List<MarkedArea> markedAreas = null;
    private String data = "";
    private String title;
    private String path;
    private AnimatorSet florMarkerAnimationSet;
    private boolean isFlowMarkerVisible = false;

    public static OverviewFragment getInstance(String title, String path){
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_ARG, title);
        bundle.putString(PATH_ARG, path);
        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = getArguments().getString(TITLE_ARG);
        path = getArguments().getString(PATH_ARG);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        text.setOnTouchListener(new OnMarkerTouchListener(getActivity(), new OnTapListener() {
            @Override
            public void onTapListener(int y) {

                int index = MarkerUtil.getMarkerIndex(markedAreas, y);

                if (index != -1) {

                    ReaderFragment fragment = ReaderFragment.getInstance(data, markers, index);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }

            }

            @Override
            public void onSwipeLeft(int y) {
                int index = MarkerUtil.getMarkerIndex(markedAreas, y);

                if (index != -1) {
                    Marker marker = markers.get(index);
                    MarkerType markerType = marker.getType();
                    App.vibratePattern(markerType.getPattern());
                }
            }

            @Override
            public void onSwipeRight(int y) {
                int index = MarkerUtil.getMarkerIndex(markedAreas, y);

                if (index != -1) {
                    Marker marker = markers.get(index);
                    MarkerType markerType = marker.getType();
                    App.vibratePattern(markerType.getPattern());
                }
            }

        }));

        markers.add(new Marker(MarkerType.GREEN, 2243, 2703));
        markers.add(new Marker(MarkerType.RED, 5500, 6100));
        markers.add(new Marker(MarkerType.BLUE, 8400, 8900));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new MainTask().execute();
            }
        }, 100);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Spannable addMarkers(List<Marker> markers) {

        Spannable spannable = Spannable.Factory.getInstance().newSpannable(data);

        for (Marker marker : markers) {

            MarkerType type = marker.getType();
            int ss = marker.getSelectionStart();
            int se = marker.getSelectionEnd();

            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(type.getBackgroundColor());
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(type.getForegroundColor());
            spannable.setSpan(backgroundColorSpan, ss, se, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(foregroundColorSpan, ss, se, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        }

        return spannable;
    }

    public interface OnTapListener {
        void onTapListener(int y);

        void onSwipeLeft(int y);

        void onSwipeRight(int y);
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

        public void onSwipeLeft(int y) {
            onTapListener.onSwipeLeft(y);
        }

        public void onSwipeRight(int y) {
            onTapListener.onSwipeRight(y);
        }

        public boolean onTouch(View v, MotionEvent event) {

            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }

            int action = event.getAction();

            int y = (int) event.getY();

            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    //flowMarker.setY(event.getY() - flowMarker.getHeight() / 2);

                    if (MarkerUtil.isInMarkedArea(markedAreas, y, 20)) {
                        MarkerType type = MarkerType.GREEN;
                        int index = MarkerUtil.getMarkerIndex(markedAreas, y);

                        if (index != -1) {
                            type = markers.get(index).getType();
                        }

                        MarkedArea markedArea = MarkerUtil.getMarkedArea(markedAreas, y, 20);
                        int y2 = (int) (markedArea.getStartY() + (markedArea.getEndY() - markedArea.getStartY()) / 2f);
                        showFlowMarker(y2, type);
                    } else {
                        hideFlowMarker();
                    }

                    if (MarkerUtil.isInMarkedArea(markedAreas, y, 15)) {
                        if (App.TPAD) {
                            ((MainActivity) getActivity()).vibrateOn();
                        } else {
                            App.vibrateOn();
                        }
                    } else {
                        if (App.TPAD) {
                            ((MainActivity) getActivity()).vibrateOff();
                        } else {
                            App.vibrateOff();
                        }
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    if (App.TPAD) {
                        ((MainActivity) getActivity()).vibrateOff();
                    } else {
                        App.vibrateOff();
                    }
                    hideFlowMarker();
                    break;
            }

            return true;
        }

        private void showFlowMarker(int y, MarkerType type) {
            if(!isFlowMarkerVisible) {
                isFlowMarkerVisible = true;
                flowMarker.setY(y - flowMarker.getHeight() / 2f);
                flowMarker.setImageResource(type.getMarkerResource());

                if(florMarkerAnimationSet == null) {
                    florMarkerAnimationSet = new AnimatorSet();
                }
                else {
//                    if (!florMarkerAnimationSet.isRunning()) {
                        florMarkerAnimationSet.cancel();
//                    }
                    florMarkerAnimationSet = new AnimatorSet();
                }
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(flowMarker, "scaleX", 0f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(flowMarker, "scaleY", 0f, 1f);
                florMarkerAnimationSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        flowMarker.setVisibility(View.VISIBLE);
                    }
                });
                florMarkerAnimationSet.playTogether(scaleX, scaleY);
                florMarkerAnimationSet.start();
            }
        }

        private void hideFlowMarker() {
            if(isFlowMarkerVisible) {
                isFlowMarkerVisible = false;
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(flowMarker, "scaleX", 1f, 0f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(flowMarker, "scaleY", 1f, 0f);

                if (florMarkerAnimationSet == null) {
                    florMarkerAnimationSet = new AnimatorSet();
                } else {
                    florMarkerAnimationSet.cancel();
                    florMarkerAnimationSet = new AnimatorSet();
                }

                florMarkerAnimationSet.playTogether(scaleX, scaleY);
                florMarkerAnimationSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        flowMarker.setVisibility(View.INVISIBLE);
                    }
                });
                florMarkerAnimationSet.setStartDelay(200);
                florMarkerAnimationSet.start();
            }
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                onTap(e);
                return true;
            }

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

    private void showLoading(){
        loadingLayout.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(loadingLayout, "alpha", 0f, 1f).start();
    }

    private void hideLoading(){
        loadingLayout.setVisibility(View.VISIBLE);
        ObjectAnimator oa = ObjectAnimator.ofFloat(loadingLayout, "alpha", 1f, 0f);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loadingLayout.setVisibility(View.GONE);
            }
        });
        oa.start();
    }

    private class MainTask extends AsyncTask<Void, Void, Spannable> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Spannable doInBackground(Void... voids) {

            AssetManager assetManager = getActivity().getAssets();
            InputStream is = null;
            try {
                is = assetManager.open("text.html");
                data = IOUtils.toString(is);
            } catch (IOException e) {

            }

            return addMarkers(markers);
        }

        @Override
        protected void onPostExecute(Spannable spannable) {

            text.setText(spannable);

            ViewTreeObserver vto = text.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    text.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    new MarkerAreasTask().execute();
                }
            });
        }

    }

    private class MarkerAreasTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            if (markedAreas == null) {
                Bitmap bitmap = ViewUtil.getViewBitmap(text);
                markedAreas = MarkerUtil.getMarkedAreasFromBitmap(bitmap);
                Log.d("HM", "markedAreas: " + markedAreas.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideLoading();
        }
    }

}
