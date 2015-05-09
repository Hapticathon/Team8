package com.appchance.hapticmarkers.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.appchance.hapticmarkers.R;
import com.appchance.hapticmarkers.models.Document;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

@LayoutId(R.layout.list_item_document)
public class DocumentViewHolder extends ItemViewHolder<Document> {

    @ViewId(R.id.iv_letter)
    ImageView letter;

    @ViewId(R.id.tv_title)
    TextView title;

    public DocumentViewHolder(View view) {
        super(view);
    }

    @Override
    public void onSetValues(Document document, PositionInfo positionInfo) {

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(document.getTitle());

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .endConfig()
                .buildRound(String.valueOf(document.getTitle().charAt(0)).toUpperCase(), color);

        letter.setImageDrawable(drawable);
        title.setText(document.getTitle());

    }
}
