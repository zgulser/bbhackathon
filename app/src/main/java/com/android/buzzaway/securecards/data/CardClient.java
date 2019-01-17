package com.android.buzzaway.securecards.data;

import android.support.annotation.Nullable;

import com.android.buzzaway.securecards.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Backbase R&D B.V. on 17/01/2019.
 */
public class CardClient {

    public static final CardClient instance = new CardClient();
    private final List<CardModel> cardModels = new ArrayList<>();

    private CardClient() {
        cardModels.add(new CardModel(1, "Visa", "Apply restrictions to my super Visa", R.drawable.visa1));
        cardModels.add(new CardModel(2, "Master", "Apply restrictions to my super Master", R.drawable.master1));
    }

    public List<CardModel> getCards() {
        return cardModels;
    }

    @Nullable
    public CardModel findCardById(long id) {
        CardModel cardModel = null;
        Iterator<CardModel> cardModelIterator = cardModels.iterator();
        while (cardModelIterator.hasNext() && (cardModel = cardModelIterator.next()).id != id) ;
        return cardModel;
    }
}
