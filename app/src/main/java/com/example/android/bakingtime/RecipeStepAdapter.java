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

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.StepItemViewHolder> {
    private Context mContext;
    private List<RecipeSteps> mRecipeStep;
    private StepItemClickListener mStepItemClickListener;

    public RecipeStepAdapter(Context mContext, List<RecipeSteps> mRecipeStep, StepItemClickListener mStepItemClickListener) {
        this.mContext = mContext;
        this.mRecipeStep = mRecipeStep;
        this.mStepItemClickListener = mStepItemClickListener;
    }

    //Will return the new view holder and inflate the Recipe Step card layout
    @NonNull
    @Override
    public StepItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_item_steps, parent, false);
        StepItemViewHolder stepItemViewHolder = new StepItemViewHolder(view);
        return stepItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepItemViewHolder holder, int position) {
        holder.stepTitleTV.setText(mRecipeStep.get(position).getStepName());
    }

    @Override
    public int getItemCount() {
        return mRecipeStep.size();
    }

    public interface StepItemClickListener{
        void onStepItemClick(int clickedItemIndex);
    }

    //Setting click listener for each RecyclerView item
    public class StepItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView stepTitleTV;
        ImageView arrowIV;

        public StepItemViewHolder(View itemView) {
            super(itemView);
            stepTitleTV = itemView.findViewById(R.id.step_title_step_card_tv);
            arrowIV = itemView.findViewById(R.id.arrow_steps_card_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mStepItemClickListener.onStepItemClick(position);
        }
    }

}
