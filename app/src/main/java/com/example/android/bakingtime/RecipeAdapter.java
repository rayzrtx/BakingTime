package com.example.android.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context mContext;
    private List<Recipe> mRecipes;
    private final RecipeItemClickListener mOnClickListener;

    public interface RecipeItemClickListener {
        void onRecipeItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(Context mContext, List<Recipe> mRecipes, RecipeItemClickListener listener) {
        this.mContext = mContext;
        this.mRecipes = mRecipes;
        mOnClickListener = listener;
    }

    //Will return the new view holder and inflate the recipe card layout
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        boolean attachToRoot = false;
        int layoutForRecipeCard = R.layout.cardview_item_recipe;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutForRecipeCard, parent, attachToRoot);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    //Will bind the parsed JSON data to the correct views
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.dessertNameTV.setText(mRecipes.get(position).getDessertName());

    }


    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    //Setting click listener for each RecyclerView item
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dessertNameTV;
        TextView dessertDescriptionTV;
        ImageView dessertIV;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            dessertNameTV = itemView.findViewById(R.id.dessert_name_tv);
            dessertDescriptionTV = itemView.findViewById(R.id.dessert_description_tv);
            dessertIV = itemView.findViewById(R.id.recipe_card_iv);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onRecipeItemClick(position);

        }
    }

}
