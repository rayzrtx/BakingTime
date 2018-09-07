package com.example.android.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    //Testing that RecipeDetailsActivity is launched from MainActivity when a Recipe is clicked
    @Test
    public void validateIntentSentOnRecipeClick(){
        Espresso.onView(ViewMatchers.withId(R.id.recipe_rv)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(RecipeDetailsActivity.class.getName()));
    }
}
