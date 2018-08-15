package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private String dessertName;
    private List<RecipeIngredients> recipeIngredients;
    private List<RecipeSteps> recipeSteps;
    private int numberOfServings;

    //Constructor
    public Recipe(String dessertName, List<RecipeIngredients> recipeIngredients,
                  List<RecipeSteps> recipeSteps, int numberOfServings) {
        this.dessertName = dessertName;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
        this.numberOfServings = numberOfServings;
    }

    public String getDessertName() {
        return dessertName;
    }

    public void setDessertName(String dessertName) {
        this.dessertName = dessertName;
    }


    public List<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<RecipeSteps> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //write object values to parcel for storage
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(dessertName);
        parcel.writeTypedList(recipeIngredients);
        parcel.writeTypedList(recipeSteps);
        parcel.writeInt(numberOfServings);
    }

    public Recipe(Parcel parcel){
        dessertName = parcel.readString();
        recipeIngredients = new ArrayList<>();
        parcel.readTypedList(recipeIngredients, RecipeIngredients.CREATOR);
        recipeSteps = new ArrayList<>();
        parcel.readTypedList(recipeSteps, RecipeSteps.CREATOR);
        numberOfServings = parcel.readInt();
    }

    //Will bind everything together when un-parceling the parcel and creating the Recipe
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[0];
        }
    };
}
