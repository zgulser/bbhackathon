package com.android.buzzaway.securecards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.android.buzzaway.securecards.data.CardClient;
import com.android.buzzaway.securecards.data.CardModel;

import java.util.Objects;

public class CardRestrictionActivity extends AppCompatActivity {

    public static final String ARG_CARD_ID = "hack.card.id";

    public static void start(@NonNull Context context, @NonNull CardModel cardModel) {
        Bundle args = new Bundle();
        args.putLong(ARG_CARD_ID, cardModel.id);
        Intent startCardRestriction = new Intent(context, CardRestrictionActivity.class);
        startCardRestriction.putExtras(args);
        context.startActivity(startCardRestriction);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_restriction);

        long id = getIntent().getLongExtra(ARG_CARD_ID, -1);
        CardModel cardModel = CardClient.instance.findCardById(id);
        Objects.requireNonNull(cardModel);
    }
}
