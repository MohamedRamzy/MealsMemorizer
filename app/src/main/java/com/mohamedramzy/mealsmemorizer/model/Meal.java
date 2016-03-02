package com.mohamedramzy.mealsmemorizer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mmahfouz on 3/2/2016.
 */
public class Meal implements Parcelable {

//    private static final int NO_FAV = 0;
//    private static final int FAV = 1;

    private int id;
    private String name;
    int fav;

    public Meal(){

    }
    public Meal(int id, String name, int fav) {
        this.id = id;
        this.name = name;
        this.fav = fav;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel source) {
            Meal meal = new Meal();
            meal.setId(source.readInt());
            meal.setName(source.readString());
            meal.setFav(source.readInt());
            return meal;
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeInt(getFav());
    }
}
