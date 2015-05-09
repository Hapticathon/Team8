package com.appchance.hapticmarkers.ui.fragments;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.appchance.hapticmarkers.models.Marker;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Pawe³ on 2015-05-09.
 */
public class ReaderFragment extends Fragment {

    private static final String FULL_TEXT_ARG = "full_text_arg";
    private static final String MARKER_LIST_ARG = "marker_list_arg";
    private static final int CHANGE_PAGE_MARGIN = 150;
    private static final int TEXT_OFFSET_CHARACTERS = 100;

    private List<Marker> markerList;
    private String fullText;
    private String cropText;
    private int startTextPosition, endTextPosition;

    @InjectView(R.id.tv_full_text) TextView fullTextView;

    public static ReaderFragment getInstance(String fullText, ArrayList<Marker> markerList){
        Bundle bundle = new Bundle();
        bundle.putString(FULL_TEXT_ARG, fullText);
        bundle.putParcelableArrayList(MARKER_LIST_ARG, markerList);
        ReaderFragment fragment = new ReaderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, container, false);
        ButterKnife.inject(this, view);
        markerList = getArguments().getParcelableArrayList(MARKER_LIST_ARG);
        fullText = getArguments().getString(FULL_TEXT_ARG);
        startTextPosition = Math.max(markerList.get(0).getSelectionStart() - TEXT_OFFSET_CHARACTERS, 0);
        endTextPosition = Math.min(markerList.get(0).getSelectionEnd() + TEXT_OFFSET_CHARACTERS, fullText.length()-1);

        cropText = fullText.substring(startTextPosition, endTextPosition);
        fullTextView.setText(cropText);
        initText();
        return view;
    }

    private void initText(){
        Spannable spannable = null;

        if (fullTextView.getText() instanceof Spannable) {
            spannable = (Spannable) fullTextView.getText();
        } else {
            spannable = Spannable.Factory.getInstance().newSpannable(fullTextView.getText());
        }

        for(Marker marker : markerList){
            if(isMarkerVisible(marker)) {
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(marker.getType().getBackgroundColor());
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(marker.getType().getForegroundColor());
                spannable.setSpan(backgroundColorSpan, marker.getSelectionStart(), marker.getSelectionEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(foregroundColorSpan, marker.getSelectionStart(), marker.getSelectionEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        fullTextView.setText(spannable);

    }

    private boolean isMarkerVisible(Marker marker){
        if(marker.getSelectionStart() >= startTextPosition || marker.getSelectionEnd() <= endTextPosition){
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fullTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE) {

                    if (event.getY() < CHANGE_PAGE_MARGIN) {
                        //PREVIOUS PAGE
                        App.vibrateOn();
                    } else if (event.getY() > fullTextView.getHeight() - CHANGE_PAGE_MARGIN) {
                        //NEXT PAGE
                        App.vibrateOn();
                    }else{
                        App.vibrateOff();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    App.vibrateOff();
                }

                return true;
            }
        });
    }
}
