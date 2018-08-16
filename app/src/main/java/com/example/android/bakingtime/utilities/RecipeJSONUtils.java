package com.example.android.bakingtime.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.Recipe;
import com.example.android.bakingtime.RecipeIngredients;
import com.example.android.bakingtime.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeJSONUtils {
    //Recipe
    private static final String DESSERT_NAME_KEY = "name";
    private static final String RECIPE_INGREDIENTS_ARRAY_KEY = "ingredients";
    private static final String RECIPE_STEPS_ARRAY_KEY = "steps";
    private static final String RECIPE_SERVINGS_KEY = "servings";
    private static final String RECIPE_IMAGE_KEY = "image";
    //Recipe Ingredients
    private static final String RECIPE_INGREDIENTS_QUANTITY_KEY = "quantity";
    private static final String RECIPE_INGREDIENTS_UNIT_OF_MEASURE_KEY = "measure";
    private static final String RECIPE_INGREDIENTS_INGREDIENT_NAME_KEY = "ingredient";
    //Recipe Steps
    private static final String RECIPE_STEPS_TITLE_KEY = "shortDescription";
    private static final String RECIPE_STEPS_DESCRIPTION_KEY = "description";
    private static final String RECIPE_STEPS_VIDEO_URL_KEY = "videoURL";
    private static final String RECIPE_STEPS_IMAGE_URL_KEY = "thumbnailURL";


    public static List<Recipe> parseRecipeJSON(String recipeJSON){
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeIngredients> recipeIngredients = new ArrayList<>();
        List<RecipeSteps> recipeSteps = new ArrayList<>();


        try {
            JSONArray rootArray = new JSONArray(recipeJSON);

            for (int i = 0; i < rootArray.length(); i++){
                JSONObject resultsObject =  rootArray.getJSONObject(i);
                String dessertName = resultsObject.getString(DESSERT_NAME_KEY);

                JSONArray ingredientsArray = resultsObject.getJSONArray(RECIPE_INGREDIENTS_ARRAY_KEY);
                for (int ii = 0; ii < ingredientsArray.length(); ii++){
                    JSONObject ingredientsObject = ingredientsArray.getJSONObject(ii);
                    String ingredientName = ingredientsObject.getString(RECIPE_INGREDIENTS_INGREDIENT_NAME_KEY);
                    int ingredientQuantity = ingredientsObject.getInt(RECIPE_INGREDIENTS_QUANTITY_KEY);
                    String unitOfMeasure = ingredientsObject.getString(RECIPE_INGREDIENTS_UNIT_OF_MEASURE_KEY);
                    RecipeIngredients newRecipeIngredients = new RecipeIngredients(ingredientName, ingredientQuantity, unitOfMeasure);
                    recipeIngredients.add(newRecipeIngredients);
                }
                JSONArray stepsArray = resultsObject.getJSONArray(RECIPE_STEPS_ARRAY_KEY);
                for (int iii = 0; iii < stepsArray.length(); iii++){
                    JSONObject stepsObject = stepsArray.getJSONObject(iii);
                    String stepsTitle = stepsObject.getString(RECIPE_STEPS_TITLE_KEY);
                    String stepsDescription = stepsObject.getString(RECIPE_STEPS_DESCRIPTION_KEY);
                    String stepsVideoURL = stepsObject.getString(RECIPE_STEPS_VIDEO_URL_KEY);
                    String stepsImageURL = stepsObject.getString(RECIPE_STEPS_IMAGE_URL_KEY);
                    RecipeSteps newRecipeSteps = new RecipeSteps(stepsTitle, stepsDescription, stepsVideoURL, stepsImageURL);
                    recipeSteps.add(newRecipeSteps);
                }
                int numberOfServings = resultsObject.getInt(RECIPE_SERVINGS_KEY);
                String dessertImage = resultsObject.getString(RECIPE_IMAGE_KEY);

                Recipe newRecipe = new Recipe(dessertName, recipeIngredients, recipeSteps, numberOfServings, dessertImage);
                recipes.add(newRecipe);

            }


        } catch (JSONException e){
            Log.e("JSON Utils", "Problem parsing the JSON results", e);
            return null;
        }
        return recipes;
    }

}
