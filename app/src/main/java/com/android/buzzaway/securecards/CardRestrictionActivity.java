package com.android.buzzaway.securecards;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.android.buzzaway.securecards.data.CardClient;
import com.android.buzzaway.securecards.data.CardModel;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.Objects;

public class CardRestrictionActivity extends AppCompatActivity {

    public static final String ARG_CARD_ID = "hack.card.id";
    public static final String ARG_LOCATION_LAT = "hack.location.lat";
    public static final String ARG_LOCATION_LNG = "hack.location.lng";
    public static final String ARG_RADIUS = "hack.location.radius";

    public static void start(@NonNull Context context, @NonNull CardModel cardModel) {
        Bundle args = new Bundle();
        args.putLong(ARG_CARD_ID, cardModel.id);
        Intent startCardRestriction = new Intent(context, CardRestrictionActivity.class);
        startCardRestriction.putExtras(args);
        context.startActivity(startCardRestriction);
    }

    public static void start(@NonNull Context context, @NonNull Location location, float radius, @NonNull CardModel cardModel) {
        Bundle args = new Bundle();
        args.putLong(ARG_CARD_ID, cardModel.id);
        args.putDouble(ARG_LOCATION_LAT, location.getLatitude());
        args.putDouble(ARG_LOCATION_LNG, location.getLongitude());
        args.putFloat(ARG_RADIUS, radius);
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

        // get locations and radius if present, then send to server when implemented

        final CharSequence[] defaultTimeRanges = new CharSequence[]{"Today", "Tom", "Week", "Month", "More"};
        MultiStateToggleButton button = this.findViewById(R.id.mstb_multi_id);
        button.setElements(defaultTimeRanges);

        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {

            }
        });

    }


}
