package com.android.buzzaway.securecards.data;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by Backbase R&D B.V. on 17/01/2019.
 */
public class CardModel {

    public final long id;
    public final @NonNull
    String title;
    public final @NonNull
    String description;
    public final @DrawableRes
    int drawable;

    public CardModel(long id, @NonNull String title, @NonNull String description, int drawable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.drawable = drawable;
    }

    @Override
    public String toString() {
        return "CardModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", drawable=" + drawable +
                '}';
    }
}
