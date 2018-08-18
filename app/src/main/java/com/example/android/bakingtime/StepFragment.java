package com.example.android.bakingtime;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {
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

    //RecyclerView variables
    @BindView(R.id.steps_recyclerview_details_fragment)
    RecyclerView mStepsRecyclerView;
    @BindView(R.id.step_title_step_card_tv)
    TextView mStepTitle;
    List<RecipeSteps> mRecipeStep;
    RecipeStepAdapter mAdapter;

    LinearLayoutManager mStepsLayoutManager;

    //Recipe that was clicked
    Recipe mRecipe;


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



        return rootView;
    }
}
