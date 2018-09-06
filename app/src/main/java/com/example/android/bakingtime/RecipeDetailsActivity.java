package com.example.android.bakingtime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity {
    private Recipe mRecipe;
    private String mDessertImageURL;
    private Boolean mTwoPaneLayout;

    int mClickedStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //Determine if using a tablet or not
        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            mTwoPaneLayout = true;
        } else {
            mTwoPaneLayout = false;
        }

        //Receiving clicked Recipe info from Main Activity
        Intent receivingIntent = getIntent();
        if (receivingIntent != null) {
            mRecipe = receivingIntent.getParcelableExtra("Recipe");
            mDessertImageURL = receivingIntent.getStringExtra("ImageURL");
        }

        //Bundle for StepFragment layout
        Bundle recipeBundle = new Bundle();
        recipeBundle.putParcelable("Recipe", mRecipe);
        recipeBundle.putString("ImageURL", mDessertImageURL);

        //If using a tablet, then set up fragments using two pane layout
        if (mTwoPaneLayout) {
            StepFragment stepFragment = new StepFragment();
            FragmentManager fragmentManager = getFragmentManager();
            //Use bundle data to populate StepFragment views
            stepFragment.setArguments(recipeBundle);

            //add bundle for initial tablet StepDetailFragment to load intro video
            Bundle stepDetailsBundle = new Bundle();
            stepDetailsBundle.putParcelable("recipe", mRecipe);
            mClickedStepIndex = 0;
            stepDetailsBundle.putInt("index", mClickedStepIndex);

            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setArguments(stepDetailsBundle);

            if (savedInstanceState == null) {
                //Load master list fragment with StepFragment info
                fragmentManager.beginTransaction()
                        .replace(R.id.master_list_fragment, stepFragment)
                        .commit();

                //Load detail fragment with StepDetailsFragment info
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment, stepDetailsFragment)
                        .commit();
            }

            //Otherwise, set up StepFragment for clicked recipe on normal phone layout
        } else {

            StepFragment stepFragment = new StepFragment();

            FragmentManager fragmentManager = getFragmentManager();
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_details_fragment, stepFragment)
                        .commit();
            }


            //Set up button on action bar if not null
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            stepFragment.setArguments(recipeBundle);

        }
    }

    //Used to set action bar to Recipe name from fragment
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
