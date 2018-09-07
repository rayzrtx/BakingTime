package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeIngredients implements Parcelable {

    private String ingredientName;
    private Double ingredientQuantity;
    private String ingredientUnitOfMeasure;

    //Constructor
    public RecipeIngredients(String ingredientName, Double ingredientQuantity, String ingredientUnitOfMeasure) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientUnitOfMeasure = ingredientUnitOfMeasure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(Double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getIngredientUnitOfMeasure() {
        return ingredientUnitOfMeasure;
    }

    public void setIngredientUnitOfMeasure(String ingredientUnitOfMeasure) {
        this.ingredientUnitOfMeasure = ingredientUnitOfMeasure;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //write object values to parcel for storage
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredientName);
        parcel.writeDouble(ingredientQuantity);
        parcel.writeString(ingredientUnitOfMeasure);
    }

    //constructor that will be collecting values sent to receiving intent
    public RecipeIngredients(Parcel parcel) {
        ingredientName = parcel.readString();
        ingredientQuantity = parcel.readDouble();
        ingredientUnitOfMeasure = parcel.readString();
    }

    //Will bind everything together when un-parceling the parcel and creating the Recipe
    public static final Parcelable.Creator<RecipeIngredients> CREATOR = new Parcelable.Creator<RecipeIngredients>() {
        @Override
        public RecipeIngredients createFromParcel(Parcel parcel) {
            return new RecipeIngredients(parcel);
        }

        @Override
        public RecipeIngredients[] newArray(int i) {
            return new RecipeIngredients[0];
        }
    };

}
