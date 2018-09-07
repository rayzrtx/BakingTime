package com.example.android.bakingtime.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkQueryUtils {

    public static final String RECIPE_URL = URLConstant.RECIPE_JSON_URL;

    //This will build the URL for the Recipes
    public static URL buildRecipeURL() {
        Uri recipeQueryUri = Uri.parse(RECIPE_URL);
        URL recipeURL = null;

        try {
            recipeURL = new URL(recipeQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return recipeURL;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
