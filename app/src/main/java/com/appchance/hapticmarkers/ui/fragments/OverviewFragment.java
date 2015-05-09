package com.appchance.hapticmarkers.ui.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appchance.hapticmarkers.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OverviewFragment extends Fragment {

    @InjectView(R.id.text)
    TextView text;

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

    }

}
