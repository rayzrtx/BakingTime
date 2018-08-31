package com.example.android.bakingtime;


import android.content.res.Configuration;
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

    String mStepDescription;
    String mVideoURL;

    @Nullable
    @BindView(R.id.step_details_tv_details_fragment)
    TextView mStepDescriptionTV;
    @Nullable
    @BindView(R.id.exo_player_step_detail_fragment)
    SimpleExoPlayerView mExoPlayerView;
    SimpleExoPlayer mExoPlayer;

    @Nullable
    @BindView(R.id.previous_button_step_details_fragment)
    Button mPreviousButton;
    @Nullable
    @BindView(R.id.next_button_step_details_fragment)
    Button mNextButton;
    @Nullable
    @BindView(R.id.exoplayer_fullscreen)
    SimpleExoPlayerView mExoPlayerFullScreen;

    View rootView;


    public StepDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Receive Step details from Bundle sent by RecipeStepDetailsActivity
        mRecipe = getArguments().getParcelable("recipe");
        mRecipeSteps = mRecipe.getRecipeSteps();

        int orientation = getResources().getConfiguration().orientation;

        //When orientation changes
        if (savedInstanceState != null) {
            //Use the step you were on when orientation changed
            mRecipeStep = savedInstanceState.getParcelable("recipe_step");
            mClickedStepIndex = savedInstanceState.getInt("clicked_index");

            //Set up fullscreen playback when orientation is changed to landscape
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rootView = inflater.inflate(R.layout.exoplayer_fullscreen, container, false);
                ButterKnife.bind(this, rootView);
                updateFullscreenUI(mRecipeStep);
            } else {
                //Inflate the fragment view
                rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
                ButterKnife.bind(this, rootView);

                //Set listener on Previous button to go back a step and show those details
                mPreviousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Release player so that new video will load
                        releasePlayer();
                        mClickedStepIndex = mClickedStepIndex - 1;
                        mRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                        updateUI(mRecipeStep);
                    }
                });

                //Set listener on Next button to go forward a step and show those details
                mNextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Release player so that new video will load
                        releasePlayer();
                        mClickedStepIndex = mClickedStepIndex + 1;
                        mRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                        updateUI(mRecipeStep);
                    }
                });

                updateUI(mRecipeStep);
            }

        } else { //If there is no saved instance state then load using bundle data
            //If phone starts in landscape mode then use the data from the bundle
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rootView = inflater.inflate(R.layout.exoplayer_fullscreen, container, false);
                ButterKnife.bind(this, rootView);
                mClickedStepIndex = getArguments().getInt("index");
                mRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                updateFullscreenUI(mRecipeStep);
            } else {
                //Inflate the fragment view
                rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
                ButterKnife.bind(this, rootView);
                mClickedStepIndex = getArguments().getInt("index");
                mRecipeStep = mRecipeSteps.get(mClickedStepIndex);

                //Set listener on Previous button to go back a step and show those details
                mPreviousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Release player so that new video will load
                        releasePlayer();
                        mClickedStepIndex = mClickedStepIndex - 1;
                        mRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                        updateUI(mRecipeStep);
                    }
                });

                //Set listener on Next button to go forward a step and show those details
                mNextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Release player so that new video will load
                        releasePlayer();
                        mClickedStepIndex = mClickedStepIndex + 1;
                        mRecipeStep = mRecipeSteps.get(mClickedStepIndex);
                        updateUI(mRecipeStep);
                    }
                });

                updateUI(mRecipeStep);
            }
        }
        return rootView;
    }


    //Save the most recently viewed step on orientation change
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe_step", mRecipeStep);
        outState.putInt("clicked_index", mClickedStepIndex);

    }

    private void updateUI(RecipeSteps clickedRecipeStep) {
        mStepDescription = clickedRecipeStep.getStepDescription();
        mStepDescriptionTV.setText(mStepDescription);

        mVideoURL = clickedRecipeStep.getStepVideoURL();
        if (mExoPlayer == null) {
            initializeExoPlayer();
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

    private void updateFullscreenUI(RecipeSteps clickedRecipeStep) {
        hideSystemUI();
        mVideoURL = clickedRecipeStep.getStepVideoURL();
        if (mExoPlayer == null) {
            initializeExoPlayerFullscreen();
        } else {
            mExoPlayer.stop();
            prepareExoPlayer(mExoPlayer);
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

    private void initializeExoPlayer() {
        //Create instance of ExoPlayer for portrait mode
        TrackSelector trackSelector = new DefaultTrackSelector();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector);
        mExoPlayerView.setPlayer(mExoPlayer);
        prepareExoPlayer(mExoPlayer);
    }

    private void initializeExoPlayerFullscreen() {
        //Create instance of ExoPlayer for landscape mode
        TrackSelector trackSelector = new DefaultTrackSelector();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector);
        mExoPlayerFullScreen.setPlayer(mExoPlayer);
        prepareExoPlayer(mExoPlayer);
    }

    private void prepareExoPlayer(SimpleExoPlayer exoPlayer) {
        com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity().getApplicationContext(),
                Util.getUserAgent(getActivity().getApplicationContext(), getString(R.string.app_name)));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Uri videoURI = Uri.parse(mVideoURL);
        MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
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
