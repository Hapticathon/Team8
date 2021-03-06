package com.appchance.hapticmarkers.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appchance.hapticmarkers.MainActivity;
import com.appchance.hapticmarkers.R;
import com.appchance.hapticmarkers.models.Document;
import com.appchance.hapticmarkers.ui.adapters.DocumentViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.ribot.easyadapter.EasyAdapter;

public class DocumentsFragment extends Fragment {

    @InjectView(R.id.lv_documents)
    ListView lvDocuments;

    private List<Document> documentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        documentList.clear();
        documentList.add(new Document("Haptic technology", "text.html"));
        documentList.add(new Document("Apache License 2.0", "text.html"));
        documentList.add(new Document("Modern technology", "text.html"));
        documentList.add(new Document("TPad specification", "text.html"));

        lvDocuments.setAdapter(new EasyAdapter<Document>(getActivity(), DocumentViewHolder.class, documentList));
        lvDocuments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String title = documentList.get(i).getTitle();
                String path = documentList.get(i).getFile();

                OverviewFragment fragment = OverviewFragment.getInstance(title, path);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(R.string.app_name);
    }
}
