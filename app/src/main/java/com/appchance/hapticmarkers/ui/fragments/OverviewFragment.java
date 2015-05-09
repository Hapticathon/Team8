package com.appchance.hapticmarkers.ui.fragments;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appchance.hapticmarkers.App;
import com.appchance.hapticmarkers.R;
import com.appchance.hapticmarkers.models.MarkedArea;
import com.appchance.hapticmarkers.utils.MarkerUtil;
import com.appchance.hapticmarkers.utils.ViewUtil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OverviewFragment extends Fragment {

    @InjectView(R.id.text)
    TextView text;

    private List<MarkedArea> markedAreas = null;

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
        String data = "";
        try {
            is = assetManager.open("text.html");
            data = IOUtils.toString(is);
        } catch (IOException e) {

        }

        text.setText(data);

        Spannable spannable = null;

        if (text.getText() instanceof Spannable) {
            spannable = (Spannable) text.getText();
        } else {
            spannable = Spannable.Factory.getInstance().newSpannable(text.getText());
        }

        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#009688"));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
        spannable.setSpan(backgroundColorSpan, 455, 531, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(foregroundColorSpan, 455, 531, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        text.setText(spannable);

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (markedAreas == null) {
                    Bitmap bitmap = ViewUtil.getViewBitmap(text);
                    markedAreas = MarkerUtil.getMarkedAreasFromBitmap(bitmap);
                }

                int action = motionEvent.getAction();

                int y = (int) motionEvent.getY();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:

                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (MarkerUtil.isInMarkedArea(markedAreas, y)) {
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
        });

    }

}
