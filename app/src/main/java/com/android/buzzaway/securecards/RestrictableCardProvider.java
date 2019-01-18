package com.android.buzzaway.securecards;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;

import java.util.Objects;

/**
 * Created by Backbase R&D B.V. on 18/01/2019.
 */
public class RestrictableCardProvider extends CardProvider<RestrictableCardProvider> {

    private boolean isRestricted;

    public boolean isRestricted() {
        return isRestricted;
    }

    public RestrictableCardProvider setRestricted(boolean isRestricted) {
        this.isRestricted = isRestricted;
        return this;
    }

    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);
        TextView restrictionSign = findViewById(view, R.id.restrictionSign, TextView.class);
        Objects.requireNonNull(restrictionSign);
        if (isRestricted()) {
            restrictionSign.setVisibility(View.VISIBLE);
        } else {
            restrictionSign.setVisibility(View.GONE);
        }
    }
}
