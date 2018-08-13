package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    private String dessertName;
    private String dessertDescription;
    private RecipeIngredients[] recipeIngredients;
    private RecipeSteps[] recipeSteps;
    private int numberOfServings;
    private String dessertImageURL;

    //Constructor
    public Recipe(String dessertName, String dessertDescription, RecipeIngredients[] recipeIngredients,
                  RecipeSteps[] recipeSteps, int numberOfServings, String dessertImageURL) {
        this.dessertName = dessertName;
        this.dessertDescription = dessertDescription;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
        this.numberOfServings = numberOfServings;
        this.dessertImageURL = dessertImageURL;
    }

    public String getDessertName() {
        return dessertName;
    }

    public void setDessertName(String dessertName) {
        this.dessertName = dessertName;
    }

    public String getDessertDescription() {
        return dessertDescription;
    }

    public void setDessertDescription(String dessertDescription) {
        this.dessertDescription = dessertDescription;
    }

    public RecipeIngredients[] getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(RecipeIngredients[] recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public RecipeSteps[] getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(RecipeSteps[] recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public String getDessertImageURL() {
        return dessertImageURL;
    }

    public void setDessertImageURL(String dessertImageURL) {
        this.dessertImageURL = dessertImageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //write object values to parcel for storage
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(dessertName);
        parcel.writeString(dessertDescription);
        parcel.writeArray(recipeIngredients);
        parcel.writeArray(recipeSteps);
        parcel.writeInt(numberOfServings);
        parcel.writeString(dessertImageURL);
    }

    public Recipe(Parcel parcel){
        dessertName = parcel.readString();
        dessertDescription = parcel.readString();
        recipeIngredients = parcel.createTypedArray(RecipeIngredients.CREATOR);
        recipeSteps = parcel.createTypedArray(RecipeSteps.CREATOR);
        numberOfServings = parcel.readInt();
        dessertImageURL = parcel.readString();
    }
}
