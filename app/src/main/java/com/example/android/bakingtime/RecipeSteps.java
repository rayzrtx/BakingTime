package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeSteps implements Parcelable{

    private String stepName;
    private String stepDescription;
    private String stepVideoURL;
    private String stepImageURL;

    //Constructor
    public RecipeSteps(String stepName, String stepDescription, String stepVideoURL, String stepImageURL) {
        this.stepName = stepName;
        this.stepDescription = stepDescription;
        this.stepVideoURL = stepVideoURL;
        this.stepImageURL = stepImageURL;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getStepVideoURL() {
        return stepVideoURL;
    }

    public void setStepVideoURL(String stepVideoURL) {
        this.stepVideoURL = stepVideoURL;
    }

    public String getStepImageURL() {
        return stepImageURL;
    }

    public void setStepImageURL(String stepImageURL) {
        this.stepImageURL = stepImageURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //write object values to parcel for storage
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(stepName);
        parcel.writeString(stepDescription);
        parcel.writeString(stepVideoURL);
        parcel.writeString(stepImageURL);
    }

    //constructor that will be collecting values sent to receiving intent
    public RecipeSteps(Parcel parcel){
        stepName = parcel.readString();
        stepDescription = parcel.readString();
        stepVideoURL = parcel.readString();
        stepImageURL = parcel.readString();
    }

    public static final Parcelable.Creator<RecipeSteps> CREATOR = new Parcelable.Creator<RecipeSteps>(){
        @Override
        public RecipeSteps createFromParcel(Parcel parcel) {
            return new RecipeSteps(parcel);
        }

        @Override
        public RecipeSteps[] newArray(int i) {
            return new RecipeSteps[0];
        }
    };
}
