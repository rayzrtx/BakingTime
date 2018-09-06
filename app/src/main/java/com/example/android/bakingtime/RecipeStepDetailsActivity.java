package com.example.android.bakingtime;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeStepDetailsActivity extends AppCompatActivity {
    Recipe mRecipe;
    int mClickedStepIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);

        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

        FragmentManager fragmentManager = getFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.step_details_fragment, stepDetailsFragment)
                    .commit();
        }

        //Set up button on action bar if not null
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Receive intent from clicked step in StepFragment
        Intent clickedStepIntent = getIntent();
        if (clickedStepIntent != null) {
            mRecipe = clickedStepIntent.getParcelableExtra("recipe");
            mClickedStepIndex = clickedStepIntent.getIntExtra("index", 0);
        }

        //Sending intent info to fragment as a bundle
        Bundle recipeStepBundle = new Bundle();
        recipeStepBundle.putParcelable("recipe", mRecipe);
        recipeStepBundle.putInt("index", mClickedStepIndex);
        stepDetailsFragment.setArguments(recipeStepBundle);
    }

    //Used to set action bar to Recipe name from fragment
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
