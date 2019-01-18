package com.android.buzzaway.securecards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.buzzaway.securecards.data.CardClient;
import com.android.buzzaway.securecards.data.CardModel;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

public class CardsActivity extends AppCompatActivity {

    public static void start(@NonNull Context context) {
        context.startActivity(new Intent(context, ConfirmationActivity.class));
    }

    private MaterialListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        listView = findViewById(R.id.cardList);

        setupCards();
    }

    private Card bindCard(final CardModel cardModel) {
        return new Card.Builder(this)
                .withProvider(new CardProvider())
                .setLayout(R.layout.bank_card_item)
                .setTitle(cardModel.title)
                .setDescription(cardModel.description)
                .setDrawable(cardModel.drawable)
                .addAction(R.id.left_text_button, new TextViewAction(this)
                        .setText("AMOUNT")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                CardRestrictionActivity.start(CardsActivity.this, cardModel);
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(this)
                        .setText("AREA")
                        .setTextResourceColor(R.color.accent_material_dark)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                MapsActivity.start(CardsActivity.this, cardModel);
                            }
                        }))
                .endConfig()
                .build();
    }

    private void setupCards() {
        for (CardModel cardModel : CardClient.instance.getCards()) {
            listView.getAdapter().add(bindCard(cardModel));
        }
    }
}
