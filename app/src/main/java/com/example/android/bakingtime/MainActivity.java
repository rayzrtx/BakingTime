package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingtime.utilities.NetworkQueryUtils;
import com.example.android.bakingtime.utilities.RecipeJSONUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    //Recyclerview which will hold the list of recipe cards
    @BindView(R.id.recipe_rv)
    RecyclerView mRecipeListRecyclerView;

    private List<Recipe> mRecipes;
    private RecipeAdapter mRecipeAdapter;

    String[] dessertImageURLs;
    String[] dessertDescriptions;
    LinearLayoutManager linearLayoutManager;

    private static final String SCROLL_POSITION_KEY = "scroll_position";
    Parcelable mLayoutManagerSavedState;

    @BindView(R.id.no_internet_tv)
    TextView mNoInternetTV;
    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTV;
    @BindView(R.id.loading_spinner)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Recyclerview is invisible until data is retreived
        mRecipeListRecyclerView.setVisibility(View.INVISIBLE);

        mRecipes = new ArrayList<>();

        //Get the array resources so that adapter can bind to appropriate views
        dessertImageURLs = getResources().getStringArray(R.array.dessert_picture_urls);
        dessertDescriptions = getResources().getStringArray(R.array.dessert_descriptions);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        //Loading previous layout state
        if (savedInstanceState != null) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(SCROLL_POSITION_KEY);
        }


        makeRecipeSearchQuery();

        //Once data has loaded, load state of any previous layouts (including scroll position)
        if (mLayoutManagerSavedState != null) {
            linearLayoutManager.onRestoreInstanceState(mLayoutManagerSavedState);
        }

        //If there is no internet connection, show No Internet message.
        if (!isConnected(MainActivity.this)) {

            showNoInternetMessage();

        }
    }

    //Save state of layout
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SCROLL_POSITION_KEY, linearLayoutManager.onSaveInstanceState());
    }

    //Will return the URL to download Recipe json info
    void makeRecipeSearchQuery() {
        URL builtURL = NetworkQueryUtils.buildRecipeURL();
        new RecipeQueryTask().execute(builtURL);
    }

    //Add intent here
    @Override
    public void onRecipeItemClick(int clickedItemIndex) {
        Intent recipeIntent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
        recipeIntent.putExtra("Recipe", mRecipes.get(clickedItemIndex));
        recipeIntent.putExtra("ImageURL", dessertImageURLs[clickedItemIndex]);
        startActivity(recipeIntent);
    }

    //Checking if device is connected to the internet
    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        //checking if connected via mobile network or wifi and if so, obtain the type of connection
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            //If mobile connection or wifi connection is connected or attempting to connect, return true for connected
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else
                return false;

        } else
            return false;
    }

    //Shown when there was an issue retrieving recipe data
    private void showErrorMessage() {
        mRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTV.setVisibility(View.VISIBLE);
        mNoInternetTV.setVisibility(View.INVISIBLE);
    }

    //Shown when there is no internet connection detected
    private void showNoInternetMessage() {
        mRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTV.setVisibility(View.INVISIBLE);
        mNoInternetTV.setVisibility(View.VISIBLE);
    }

    private void showRecipeDataView() {
        mRecipeListRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTV.setVisibility(View.INVISIBLE);
        mNoInternetTV.setVisibility(View.INVISIBLE);
    }

    //AsyncTask to perform network request in background thread
    public class RecipeQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        //Will connect to URL and return JSON string info
        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];

            String recipeResults = null;

            try {
                recipeResults = NetworkQueryUtils.getResponseFromHttpUrl(searchURL); //Retrieving JSON from URL
            } catch (IOException e) {
                e.printStackTrace();
            }
            return recipeResults; //JSON string with all of the retrieved JSON data that now needs to be parsed
        }

        @Override
        protected void onPostExecute(String recipeResults) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (recipeResults != null && recipeResults != "") {
                //Set recyclerview to visible
                showRecipeDataView();
                mRecipes = RecipeJSONUtils.parseRecipeJSON(recipeResults); //Will parse JSON data and return a list of Recipe objects
                //Bind parsed JSON data to recyclerview and use Adapter to populate UI
                mRecipeListRecyclerView.setLayoutManager(linearLayoutManager);
                mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipes, MainActivity.this, dessertImageURLs, dessertDescriptions);
                mRecipeListRecyclerView.setAdapter(mRecipeAdapter);
            } else {
                showErrorMessage();
            }
        }
    }
}
