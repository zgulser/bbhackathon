package com.android.buzzaway.securecards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.buzzaway.securecards.data.CardClient;
import com.android.buzzaway.securecards.data.CardModel;
import com.easyfingerprint.EasyFingerPrint;
import com.theophrast.ui.widget.SquareImageView;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.Objects;

public class CardRestrictionActivity extends AppCompatActivity {

    public static final String ARG_CARD_ID = "hack.card.id";
    public static final String ARG_LOCATION_LAT = "hack.location.lat";
    public static final String ARG_LOCATION_LNG = "hack.location.lng";
    public static final String ARG_LOCATION_IMG = "hack.location.img";
    public static final String ARG_RADIUS = "hack.location.radius";
    private TextView dateRangeView;
    private CardModel cardModel;

    public static void start(@NonNull Context context, @NonNull CardModel cardModel) {
        Bundle args = new Bundle();
        args.putLong(ARG_CARD_ID, cardModel.id);
        Intent startCardRestriction = new Intent(context, CardRestrictionActivity.class);
        startCardRestriction.putExtras(args);
        context.startActivity(startCardRestriction);
    }

    public static void start(@NonNull Context context, @NonNull Location location, float radius,
                             @NonNull byte[] map,
                             @NonNull CardModel cardModel) {
        Bundle args = new Bundle();
        args.putLong(ARG_CARD_ID, cardModel.id);
        args.putDouble(ARG_LOCATION_LAT, location.getLatitude());
        args.putDouble(ARG_LOCATION_LNG, location.getLongitude());
        args.putByteArray(ARG_LOCATION_IMG, map);
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
        cardModel = CardClient.instance.findCardById(id);
        Objects.requireNonNull(cardModel);

        SquareImageView mapScreenshot = findViewById(R.id.mapScreenshot);
        if (getIntent().hasExtra(ARG_LOCATION_IMG)) {
            byte[] mapBytes = getIntent().getByteArrayExtra(ARG_LOCATION_IMG);
            Bitmap map = BitmapFactory.decodeByteArray(mapBytes, 0, mapBytes.length);
            mapScreenshot.setImageBitmap(map);
        } else {
            mapScreenshot.setVisibility(View.GONE);
        }

        // get locations and radius if present, then send to server when implemented

        dateRangeView = findViewById(R.id.pickDateRange);
        dateRangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateBottomSheet();
            }
        });

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprint();
            }
        });
    }

    private void dateBottomSheet() {
        final CharSequence[] defaultDateRanges = new CharSequence[]{
                "Today", "Tomorrow", "This week", "This month", "This year", "Custom"};
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle("How long will the restriction last?");
        builder.setItems(defaultDateRanges, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == defaultDateRanges.length - 1) {
                    dateRangePicker();
                } else {
                    CharSequence range = defaultDateRanges[which];
                    dateRangeView.setText("Restriction period: " + range);
                }
            }
        });
        builder.show();
    }

    private void dateRangePicker() {

    }

    private void fingerprint() {
        new EasyFingerPrint(this)
                .setTittle("Verify with your fingerprint")
                .setSubTittle("foo@example.com")
                .setDescription("In order to use the Fingerprint sensor we need your authorization first")
                .setColorPrimary(R.color.colorPrimary)
                .setIcon(ContextCompat.getDrawable(this, R.drawable.ic_credit_card_black_24dp))
                .setListern(new EasyFingerPrint.ResultFingerPrintListern() {
                    @Override
                    public void onError(@NonNull String message, int i) {
                        Toast.makeText(CardRestrictionActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSucess(FingerprintManagerCompat.CryptoObject cryptoObject) {
                        Toast.makeText(CardRestrictionActivity.this, "Activated your restriction", Toast.LENGTH_LONG).show();
                        cardModel.setRestricted(true);
                        CardsActivity.startWithConfirmation(CardRestrictionActivity.this);
                    }
                }).startScan();
    }


}
