package com.appchance.hapticmarkers.ui.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appchance.hapticmarkers.App;
import com.appchance.hapticmarkers.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Pawe³ on 2015-05-09.
 */
public class ReaderFragment extends Fragment {

    private static final String TEXT_OFFSET_ARG = "text_offset_arg";
    private static final String FULL_TEXT_ARG = "full_text_arg";
    private static final int CHANGE_PAGE_MARGIN = 150;

    private int textOffset;
    private String fullText;

    @InjectView(R.id.tv_full_text) TextView fullTextView;

    public static ReaderFragment getInstance(String fullText, int textOffset){
        Bundle bundle = new Bundle();
        bundle.putInt(TEXT_OFFSET_ARG, textOffset);
        bundle.putString(FULL_TEXT_ARG, fullText);
        ReaderFragment fragment = new ReaderFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader, container, false);
        ButterKnife.inject(this, view);
        textOffset = getArguments().getInt(TEXT_OFFSET_ARG);
        fullText = getArguments().getString(FULL_TEXT_ARG);
        fullTextView.setText(fullText);
        return view;
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
