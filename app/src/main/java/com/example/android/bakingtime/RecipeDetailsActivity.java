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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (findViewById(R.id.two_pane_tablet_layout) != null){
            mTwoPaneLayout = true;
        }else {
            mTwoPaneLayout = false;
        }

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

        //Receiving clicked Recipe info
        Intent receivingIntent = getIntent();
        if (receivingIntent != null){
            mRecipe = receivingIntent.getParcelableExtra("Recipe");
            mDessertImageURL = receivingIntent.getStringExtra("ImageURL");
        }

        Bundle recipeBundle = new Bundle();
        recipeBundle.putParcelable("Recipe", mRecipe);
        recipeBundle.putString("ImageURL", mDessertImageURL);
        stepFragment.setArguments(recipeBundle);


    }
    //Used to set action bar to Recipe name from fragment
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
