package com.example.android.bakingtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //Recyclerview which will hold the list of recipe cards
    @BindView(R.id.recipe_rv)
    RecyclerView mRecipeListRecyclerView;

    @BindView(R.id.dessert_name_tv)
    TextView mDessertName;

    @BindView(R.id.dessert_description_tv)
    TextView mDessertDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
