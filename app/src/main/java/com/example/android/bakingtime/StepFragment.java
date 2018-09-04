package com.example.android.bakingtime;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements RecipeStepAdapter.StepItemClickListener {
    //Find each view in the layout

    @BindView(R.id.dessert_image_details_fragment_iv)
    ImageView mDessertImageIV;
    @BindView(R.id.dessert_name_details_fragment_tv)
    TextView mDessertNameTV;
    @BindView(R.id.servings_size_details_fragment_tv)
    TextView mServingSizeTV;
    @BindView(R.id.ingredients_list_details_fragment_tv)
    TextView mIngredientsListTV;
    String mQuantity;
    String mUnitOfMeasure;
    String mIngredient;
    String mDessertImageURL;

    //RecyclerView variables
    @BindView(R.id.steps_recyclerview_details_fragment)
    RecyclerView mStepsRecyclerView;
    List<RecipeSteps> mRecipeStep;
    RecipeStepAdapter mAdapter;

    LinearLayoutManager mStepsLayoutManager;
    Parcelable mSavedStateLayoutManager;

    //Recipe that was clicked
    Recipe mRecipe;

    SharedPreferences widgetSharedPreferences;
    SharedPreferences.Editor editor;

    StringBuilder ingredientsStringBuilder;


    public StepFragment() {
    }

    //Inflates the Recipe Details Fragment layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);


        ButterKnife.bind(this, rootView);

        mRecipeStep = new ArrayList<>();

        //Receiving bundle from RecipeDetailsActivity
        mRecipe = getArguments().getParcelable("Recipe");
        mDessertImageURL = getArguments().getString("ImageURL");

        //Set Action Bar to show name of clicked recipe
        ((RecipeDetailsActivity) getActivity()).setActionBarTitle(mRecipe.getDessertName());

        //Saving Dessert Name to send to widget as shared preference
        widgetSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("widget_text", Context.MODE_PRIVATE);
        editor = widgetSharedPreferences.edit();
        editor.putString("dessert_name", mRecipe.getDessertName());
        editor.apply();

        //To notify widget of the clicked recipe and to send recipe data to widget
        Intent widgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        getActivity().getApplicationContext().sendBroadcast(widgetIntent);

        //If recyclerview position has been saved, then retrieve it for loading
        if (savedInstanceState != null){
            mSavedStateLayoutManager = savedInstanceState.getParcelable("scroll_position");
        }


        updateUI(mRecipe, mDessertImageURL);

        mRecipeStep = mRecipe.getRecipeSteps();
        Context context = getActivity().getApplicationContext();
        mStepsLayoutManager = new LinearLayoutManager(context);
        mStepsRecyclerView.setLayoutManager(mStepsLayoutManager);
        mAdapter = new RecipeStepAdapter(context, mRecipeStep, StepFragment.this);
        mStepsRecyclerView.setAdapter(mAdapter);

        //Once data has loaded, load state of any previous layouts (including scroll position)
        if (mSavedStateLayoutManager != null){
            mStepsLayoutManager.onRestoreInstanceState(mSavedStateLayoutManager);
        }

        return rootView;
    }

    //Method to populate the ingredients list for the clicked recipe
    private void updateUI(Recipe clickedRecipe, String imageURL) {
        Picasso.get()
                .load(imageURL)
                .into(mDessertImageIV);

        String dessertName = clickedRecipe.getDessertName();
        mDessertNameTV.setText(dessertName);

        String servings = String.valueOf(clickedRecipe.getNumberOfServings());
        mServingSizeTV.setText(servings);

        List<RecipeIngredients> ingredients = clickedRecipe.getRecipeIngredients();
        ingredientsStringBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            mQuantity = String.valueOf(ingredients.get(i).getIngredientQuantity());
            mUnitOfMeasure = ingredients.get(i).getIngredientUnitOfMeasure();
            mIngredient = ingredients.get(i).getIngredientName();
            String concatenatedIngredients;
            if (mUnitOfMeasure.equals("UNIT")) {
                concatenatedIngredients = mQuantity + " " + mIngredient + System.lineSeparator();

            } else {
                concatenatedIngredients = mQuantity + " " + mUnitOfMeasure + " " + mIngredient + System.lineSeparator();
            }
            mIngredientsListTV.append(concatenatedIngredients);
            ingredientsStringBuilder.append(concatenatedIngredients);

        }
        //Save the ingredients list string as a shared preference to send to widget
        editor.putString("ingredientsList", ingredientsStringBuilder.toString());
        editor.apply();
    }

    //Intent to open details of clicked step including video and instructions
    @Override
    public void onStepItemClick(int clickedItemIndex) {
        Intent stepIntent = new Intent(getActivity(), RecipeStepDetailsActivity.class);
        stepIntent.putExtra("recipe", mRecipe);
        stepIntent.putExtra("index", clickedItemIndex);
        startActivity(stepIntent);
    }

    //Save scroll position of recyclerview
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("scroll_position", mStepsLayoutManager.onSaveInstanceState());
    }
}