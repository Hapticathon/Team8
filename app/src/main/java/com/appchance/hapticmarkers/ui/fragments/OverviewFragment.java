package com.appchance.hapticmarkers.ui.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
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

public class OverviewFragment extends Fragment {

    @InjectView(R.id.text)
    TextView text;

    String data = "";
    private int currentTextOffset;

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
        text.setClickable(true);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    Layout layout = ((TextView) v).getLayout();
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (layout != null) {
                        int line = layout.getLineForVertical(y);
                        int offset = layout.getOffsetForHorizontal(line, x);
                        currentTextOffset = offset;
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, ReaderFragment.getInstance(data, currentTextOffset))
                            .commit();
                }

                return true;
            }
        });

    }

}
