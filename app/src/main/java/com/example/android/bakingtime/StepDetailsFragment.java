package com.example.android.bakingtime;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends android.app.Fragment {

    //Clicked Recipe step
    RecipeSteps mRecipeStep;
    Recipe mRecipe;
    int mClickedStepIndex;
    List<RecipeSteps> mRecipeSteps;
    //New step after a navigation button is clicked
    RecipeSteps newRecipeStep;

    String mStepDescription;
    String mVideoURL;

    @BindView(R.id.step_details_tv_details_fragment)
    TextView mStepDescriptionTV;
    @BindView(R.id.exo_player_step_detail_fragment)
    SimpleExoPlayerView mExoPlayerView;
    SimpleExoPlayer mExoPlayer;

    @BindView(R.id.previous_button_step_details_fragment)
    Button mPreviousButton;
    @BindView(R.id.next_button_step_details_fragment)
    Button mNextButton;

    public StepDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment view
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, rootView);

        //Receive Step details from Bundle sent by RecipeStepDetailsActivity
        mRecipe = getArguments().getParcelable("recipe");
        mClickedStepIndex = getArguments().getInt("index");

        mRecipeSteps = mRecipe.getRecipeSteps();
        mRecipeStep = mRecipeSteps.get(mClickedStepIndex);

        //Set listener on Previous button to go back a step and show those details
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Release player so that new video will load
                releasePlayer();
                mClickedStepIndex = mClickedStepIndex - 1;
                newRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                updateUI(newRecipeStep);
            }
        });

        //Set listener on Next button to go forward a step and show those details
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Release player so that new video will load
                releasePlayer();
                mClickedStepIndex = mClickedStepIndex + 1;
                newRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                updateUI(newRecipeStep);
            }
        });

        updateUI(mRecipeStep);

        return rootView;
    }

    private void updateUI(RecipeSteps clickedRecipeStep) {
        mStepDescription = clickedRecipeStep.getStepDescription();
        mStepDescriptionTV.setText(mStepDescription);

        mVideoURL = clickedRecipeStep.getStepVideoURL();
        if (mExoPlayer == null) {
            //Create instance of ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);
            //Prepare the player
            com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity().getApplicationContext(),
                    Util.getUserAgent(getActivity().getApplicationContext(), getString(R.string.app_name)));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            Uri videoURI = Uri.parse(mVideoURL);
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }

        //If you are on the very first step, then do not show the Previous button
        if (mClickedStepIndex == 0) {
            hidePreviousButton();
        } else {
            showPreviousButton();
        }

        //If you are on the very last step, then do not show the Next button
        if (mClickedStepIndex == mRecipeSteps.size() - 1) {
            hideNextButton();
        } else {
            showNextButton();
        }
    }

    private void hidePreviousButton() {
        mPreviousButton.setVisibility(View.INVISIBLE);
        mPreviousButton.setClickable(false);
    }

    private void showPreviousButton() {
        mPreviousButton.setVisibility(View.VISIBLE);
        mPreviousButton.setClickable(true);
    }

    private void hideNextButton() {
        mNextButton.setVisibility(View.INVISIBLE);
        mNextButton.setClickable(false);
    }

    private void showNextButton() {
        mNextButton.setVisibility(View.VISIBLE);
        mNextButton.setClickable(true);
    }

    //Player must be released when not in use to save resources
    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
}
