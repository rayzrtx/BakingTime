package com.example.android.bakingtime;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

//    @BindView(R.id.dessert_name_tv)
//    TextView mDessertName;

//    @BindView(R.id.dessert_description_tv)
//    TextView mDessertDescription;

    private List<Recipe> mRecipes;
    private RecipeAdapter mRecipeAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecipes = new ArrayList<>();
        makeRecipeSearchQuery();
    }

    //Will return the URL to download Recipe json info
    void makeRecipeSearchQuery(){
        URL builtURL = NetworkQueryUtils.buildRecipeURL();
        new RecipeQueryTask().execute(builtURL);
    }

    @Override
    public void onRecipeItemClick(int clickedItemIndex) {
        Toast.makeText(this, "Recipe details coming soon!", Toast.LENGTH_SHORT).show();
    }

    //AsyncTask to perform network request in background thread
    public class RecipeQueryTask extends AsyncTask<URL, Void, String>{

        //Will connect to URL and return JSON string info
        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];

            String recipeResults = null;

            try {
                recipeResults = NetworkQueryUtils.getResponseFromHttpUrl(searchURL); //Retrieving JSON from URL
            }catch (IOException e){
                e.printStackTrace();
            }
            return recipeResults; //JSON string with all of the retrieved JSON data that now needs to be parsed
        }

        @Override
        protected void onPostExecute(String recipeResults) {
            if (recipeResults != null && recipeResults != ""){
                mRecipes = RecipeJSONUtils.parseRecipeJSON(recipeResults); //Will parse JSON data and return a list of Recipe objects
                //Bind parsed JSON data to recyclerview and use Adapter to populate UI
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                mRecipeListRecyclerView.setLayoutManager(linearLayoutManager);
                mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipes, MainActivity.this);
                mRecipeListRecyclerView.setAdapter(mRecipeAdapter);

                //try for loop to add pics and descriptions here. If not then try adapter viewholder.
            }
        }
    }
}
