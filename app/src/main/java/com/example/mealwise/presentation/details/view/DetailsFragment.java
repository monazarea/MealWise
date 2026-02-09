package com.example.mealwise.presentation.details.view;

import android.app.AlertDialog;
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
import com.example.mealwise.utils.AlertUtils;
import com.example.mealwise.utils.ImageLoader;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class DetailsFragment extends Fragment implements MealDetailsView {

    private TextView instructionLabel,ingredientLabel,videoLabel;
    private ImageView btnBack;
    private ImageView ivMealImage;
    private ImageView btnFavorite;
    private ImageView btnAddToPlan;
    private TextView tvMealName, tvMealSubtitle, tvInstructions;
    private RecyclerView rvIngredients;
    private YouTubePlayerView youTubePlayerView;
    private CardView cvVideoContainer;
    private MealDetailsPresenterImpl presenter;
    private IngredientsAdapter ingredientsAdapter;
    private Meal currentMeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        presenter = new MealDetailsPresenterImpl(this, Injection.provideMealsRepository(requireContext()));

        if (getArguments() != null) {
            DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
            currentMeal = args.getMeal();

            if (currentMeal != null) {
                if (isMealComplete(currentMeal)) {
                    showMealDetails(currentMeal);
                } else {
                    showBasicInfo(currentMeal);
                    presenter.getMealDetails(currentMeal.getId());
                }
                presenter.checkFavoriteStatus(currentMeal.getId());
            }
        }
        setupClickListeners();
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnFavorite = view.findViewById(R.id.IvAddToFav);
        btnAddToPlan = view.findViewById(R.id.IvAddToPlan);
        ivMealImage = view.findViewById(R.id.ivMealImage);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvMealSubtitle = view.findViewById(R.id.tvMealSubtitle);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        rvIngredients = view.findViewById(R.id.rvIngredients);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        cvVideoContainer = view.findViewById(R.id.cvVideoContainer);
        getLifecycle().addObserver(youTubePlayerView);
        ingredientLabel = view.findViewById(R.id.tvIngredientsLabel);
        instructionLabel = view.findViewById(R.id.headerInstructions);
        videoLabel = view.findViewById(R.id.headerVideo);

    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        btnFavorite.setOnClickListener(v -> {
            if (currentMeal != null) {
                presenter.toggleFavorite(currentMeal);
            }
        });

        btnAddToPlan.setOnClickListener(v -> {
            if (currentMeal != null) {
                showDaySelectionDialog();
            }
        });
    }
    private void showDaySelectionDialog() {
        com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog = new com.google.android.material.bottomsheet.BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_plan_calender, null);
        bottomSheetDialog.setContentView(dialogView);

        android.widget.CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        View btnConfirm = dialogView.findViewById(R.id.btnConfirmPlan);

        java.util.Calendar sharedCalendar = java.util.Calendar.getInstance();

        calendarView.setMinDate(sharedCalendar.getTimeInMillis());

        java.util.Calendar maxCalendar = (java.util.Calendar) sharedCalendar.clone();
        maxCalendar.add(java.util.Calendar.MONTH, 1);
        calendarView.setMaxDate(maxCalendar.getTimeInMillis());

        final String[] selectedDate = new String[1];
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);

        selectedDate[0] = dateFormat.format(sharedCalendar.getTime());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            sharedCalendar.set(year, month, dayOfMonth);
            selectedDate[0] = dateFormat.format(sharedCalendar.getTime());
        });

        btnConfirm.setOnClickListener(v -> {
            if (selectedDate[0] != null) {
                presenter.addToPlan(currentMeal, selectedDate[0]);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void updateFavoriteState(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_oulined);
        }
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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
        if (meal == null||getView() == null) return;
        if(meal.getInstructions() ==null) instructionLabel.setVisibility(View.GONE);
        this.currentMeal = meal;
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
            AlertUtils.showErrorSnackBar(getView(), message);
        }
    }

    private void setupIngredientsRecyclerView(Meal meal) {
        if (meal.getIngredients() != null && !meal.getIngredients().isEmpty()) {
            ingredientsAdapter = new IngredientsAdapter(requireContext(), meal.getIngredients());
            rvIngredients.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            rvIngredients.setAdapter(ingredientsAdapter);
            rvIngredients.setVisibility(View.VISIBLE);
        } else {
            ingredientLabel.setVisibility(View.GONE);
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
                videoLabel.setVisibility(View.GONE);
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
        presenter.onDestroy();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }
}