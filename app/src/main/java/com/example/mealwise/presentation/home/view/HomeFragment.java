package com.example.mealwise.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Category;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.home.presenter.HomePresenterImpl;
import com.example.mealwise.utils.AlertUtils;
import com.example.mealwise.utils.Constants;
import com.example.mealwise.utils.ImageLoader;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView ,CategoriesAdapter.OnCategoryClickListener,MealsAdapter.OnMealClickListener{

    private HomePresenterImpl presenter;

    private View loadingView;
    private View homeContent;
    private ImageView ivHeroMeal;
    private TextView tvHeroTitle;
    private CardView cvMealOfDay;
    private Button btnViewRecipe;

    private CategoriesAdapter categoriesAdapter;
    private MealsAdapter mealsAdapter;
    private RecyclerView rvCategories;
    private RecyclerView rvMeals;
    private TextView tvCategoryTitle,tvSeeAllMeals;
    private String currentCategoryName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        presenter = new HomePresenterImpl(this, Injection.provideMealsRepository());
        presenter.getHomeData();
        tvSeeAllMeals.setOnClickListener(v -> {
            if (currentCategoryName != null && !currentCategoryName.isEmpty()) {
                navigateToSeeAll(currentCategoryName);
            }
        });

    }

    private void initViews(View view) {
        loadingView = view.findViewById(R.id.loadingView);
        homeContent = view.findViewById(R.id.homeContent);

        ivHeroMeal = view.findViewById(R.id.ivHeroMeal);
        tvHeroTitle = view.findViewById(R.id.tvHeroTitle);
        cvMealOfDay = view.findViewById(R.id.cvMealOfDay);
        btnViewRecipe = view.findViewById(R.id.btnViewRecipe);

        rvCategories = view.findViewById(R.id.rvCategories);
        rvMeals = view.findViewById(R.id.rvMeals);
        tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);
        tvSeeAllMeals = view.findViewById(R.id.tvSeeAllMeals);
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        homeContent.setVisibility(View.GONE);
     //cvMealOfDay.setVisibility(View.GONE);

    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
        homeContent.setVisibility(View.VISIBLE);
   //cvMealOfDay.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        tvHeroTitle.setText(meal.getName());
        ImageLoader.loadImage(requireContext(), meal.getThumbUrl(), ivHeroMeal);

        View.OnClickListener listener = v -> navigateToDetails(meal);

        btnViewRecipe.setOnClickListener(listener);
        cvMealOfDay.setOnClickListener(listener);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoriesAdapter = new CategoriesAdapter(requireContext(), categories, this);

        rvCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void showMealsByCategory(List<Meal> meals,String categoryName) {
        this.currentCategoryName = categoryName;
        tvCategoryTitle.setText(categoryName);
        mealsAdapter = new MealsAdapter(requireContext(), meals, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rvMeals.setLayoutManager(gridLayoutManager);
        rvMeals.setAdapter(mealsAdapter);
        rvMeals.setNestedScrollingEnabled(false);
    }

    @Override
    public void showError(String message) {
        hideLoading();
        AlertUtils.showErrorSnackBar(getView(), message);
    }
    @Override
    public void navigateToDetails(Meal meal) {
        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action =
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }
    @Override
    public void navigateToSeeAll(String categoryName) {
        HomeFragmentDirections.ActionHomeFragmentToMealsListFragment action =
                HomeFragmentDirections.actionHomeFragmentToMealsListFragment(
                        Constants.TYPE_CATEGORY,
                        categoryName
                );

        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }


    @Override
    public void onCategoryClick(Category category) {
        presenter.getMealsByCategory(category.getName());
    }

    @Override
    public void onMealClick(Meal meal) {
        navigateToDetails(meal);    }
}