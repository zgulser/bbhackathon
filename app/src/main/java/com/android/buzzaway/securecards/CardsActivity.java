package com.android.buzzaway.securecards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.buzzaway.securecards.data.CardClient;
import com.android.buzzaway.securecards.data.CardModel;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

public class CardsActivity extends AppCompatActivity {

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
                        .setText("VIEW")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Log.d("ADDING", "CARD");
                                Toast.makeText(getApplicationContext(), "Added new card", Toast.LENGTH_SHORT).show();
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(this)
                        .setText("RESTRICT")
                        .setTextResourceColor(R.color.accent_material_dark)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                CardRestrictionActivity.start(CardsActivity.this, cardModel);
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
