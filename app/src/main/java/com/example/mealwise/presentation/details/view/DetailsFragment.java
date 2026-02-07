package com.example.mealwise.presentation.details.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.details.presenter.MealDetailsPresenterImpl;
import com.example.mealwise.utils.ImageLoader;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class DetailsFragment extends Fragment implements MealDetailsView {

    private ImageView btnBack;
    private ImageView ivMealImage;
    private TextView tvMealName, tvMealSubtitle, tvInstructions;
    private RecyclerView rvIngredients;
    private YouTubePlayerView youTubePlayerView;
    private CardView cvVideoContainer;

    private MealDetailsPresenterImpl presenter;
    private IngredientsAdapter ingredientsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        presenter = new MealDetailsPresenterImpl(this, Injection.provideMealsRepository());

        if (getArguments() != null) {
            DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
            Meal receivedMeal = args.getMeal();

            if (receivedMeal != null) {
                if (isMealComplete(receivedMeal)) {
                    showMealDetails(receivedMeal);
                } else {
                    showBasicInfo(receivedMeal);
                    presenter.getMealDetails(receivedMeal.getId());
                }
            }
        }
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        ivMealImage = view.findViewById(R.id.ivMealImage);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvMealSubtitle = view.findViewById(R.id.tvMealSubtitle);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        rvIngredients = view.findViewById(R.id.rvIngredients);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        cvVideoContainer = view.findViewById(R.id.cvVideoContainer);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        getLifecycle().addObserver(youTubePlayerView);
    }

    private boolean isMealComplete(Meal meal) {
        return meal.getInstructions() != null && !meal.getInstructions().isEmpty();
    }

    private void showBasicInfo(Meal meal) {
        tvMealName.setText(meal.getName());
        ImageLoader.loadImage(requireContext(), meal.getThumbUrl(), ivMealImage);
        tvMealSubtitle.setText("Loading details...");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMealDetails(Meal meal) {
        if (meal == null) return;

        tvMealName.setText(meal.getName());
        String subtitle = meal.getArea() + " | " + meal.getCategory();
        tvMealSubtitle.setText(subtitle);
        tvInstructions.setText(meal.getInstructions());

        Glide.with(this).load(meal.getThumbUrl()).into(ivMealImage);

        setupIngredientsRecyclerView(meal);
        setupVideoState(meal.getYoutubeUrl());
    }

    @Override
    public void showError(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupIngredientsRecyclerView(Meal meal) {
        if (meal.getIngredients() != null && !meal.getIngredients().isEmpty()) {
            ingredientsAdapter = new IngredientsAdapter(requireContext(), meal.getIngredients());
            rvIngredients.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            rvIngredients.setAdapter(ingredientsAdapter);
            rvIngredients.setVisibility(View.VISIBLE);
        } else {
            rvIngredients.setVisibility(View.GONE);
        }
    }
    private void setupVideoState(String videoUrl) {
        if (videoUrl != null && !videoUrl.isEmpty()) {

            String videoId = extractYoutubeVideoId(videoUrl);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    if (videoId != null && !videoId.isEmpty()) {
                        youTubePlayer.cueVideo(videoId, 0);
                    }
                }
            });

            if (getView() != null) {
                View card = getView().findViewById(R.id.cvVideoContainer);
                if(card != null) card.setVisibility(View.VISIBLE);
            }

        } else {
            if (getView() != null) {
                View card = getView().findViewById(R.id.cvVideoContainer);
                if(card != null) card.setVisibility(View.GONE);
            }
        }
    }

    private String extractYoutubeVideoId(String url) {
        String videoId = "";
        if (url != null && url.trim().length() > 0) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            java.util.regex.Pattern compiledPattern = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }
}