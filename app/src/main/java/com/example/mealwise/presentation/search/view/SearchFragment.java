package com.example.mealwise.presentation.search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealwise.R;

import com.example.mealwise.data.meals.models.Area;
import com.example.mealwise.data.meals.models.Category;
import com.example.mealwise.data.meals.models.Ingredient;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.home.view.MealsAdapter;
import com.example.mealwise.presentation.search.presenter.SearchPresenter;
import com.example.mealwise.presentation.search.presenter.SearchPresenterImpl;
import com.example.mealwise.utils.AlertUtils;
import com.example.mealwise.utils.Constants;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView {

    private SearchPresenter presenter;
    private MealsAdapter mealsAdapter;
    private SearchFilterAdapter filterAdapter;
    private RecyclerView rvSearch;
    private EditText etSearch;
    private ProgressBar progressBar;
    private ChipGroup chipGroup;
    private LinearLayout layoutNoResults;
    private ImageView btnBack;
    private View noInternetLayout;
    private Button btnRetry;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenterImpl(this, Injection.provideMealsRepository(requireContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        chipGroup.clearCheck();
        setupRecyclerView();
        presenter.prepareSearchObserver();
        setupSearchListener();
        if (etSearch.getText() != null && !etSearch.getText().toString().isEmpty()) {
            presenter.onSearchQueryChanged(etSearch.getText().toString());
        }
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        btnRetry.setOnClickListener(v -> {
            noInternetLayout.setVisibility(View.GONE);
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) {
                presenter.onSearchQueryChanged(query);
            }
        });

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipCategory) {
                //etSearch.setHint("Search by Category...");
                presenter.getCategories();
            } else if (checkedId == R.id.chipArea) {
               // etSearch.setHint("Search by Area...");
                presenter.getAreas();
            } else if (checkedId == R.id.chipIngredient) {
               // etSearch.setHint("Search by Ingredient...");
                presenter.getIngredients();
            } else {
                etSearch.setHint(getString(R.string.search_for_meals));
                clearResults();
            }
        });
    }

    private void initViews(View view) {
        rvSearch = view.findViewById(R.id.rvSearch);
        etSearch = view.findViewById(R.id.etSearch);
        progressBar = view.findViewById(R.id.progressBarSearch);
        chipGroup = view.findViewById(R.id.chipGroupSearch);
        layoutNoResults = view.findViewById(R.id.layoutNoResults);
        btnBack = view.findViewById(R.id.btnBack);
        noInternetLayout = view.findViewById(R.id.noInternetLayout);
        btnRetry = view.findViewById(R.id.btnRetry);

    }

    private void setupRecyclerView() {
        rvSearch.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        mealsAdapter = new MealsAdapter(requireContext(), new ArrayList<>(), this::navigateToDetails);

        filterAdapter = new SearchFilterAdapter((type, value) -> {
            navigateToFilterResults(type, value);
        });

        rvSearch.setAdapter(mealsAdapter);
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSearchQueryChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvSearch.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
       // showContent();
        rvSearch.setVisibility(View.VISIBLE);
        layoutNoResults.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
        mealsAdapter.setList(meals);

        rvSearch.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvSearch.setAdapter(mealsAdapter);
        mealsAdapter.setList(meals);
    }

    @Override
    public void showEmptyState() {
        //showContent();
        rvSearch.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.VISIBLE);
        noInternetLayout.setVisibility(View.GONE);
    }
    @Override
    public void showNetworkError() {
        rvSearch.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        noInternetLayout.setVisibility(View.VISIBLE);
    }
    @Override
    public void showContent() {
        noInternetLayout.setVisibility(View.GONE);
    }

    @Override
    public void clearResults() {
        mealsAdapter.setList(new ArrayList<>());
        rvSearch.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCategoriesList(List<Category> categories) {
        updateAdapterForFilters();
        filterAdapter.setData(categories, "category");
    }

    @Override
    public void showAreasList(List<Area> areas) {
        updateAdapterForFilters();
        filterAdapter.setData(areas, "area");
    }

    @Override
    public void showIngredientsList(List<Ingredient> ingredients) {
        updateAdapterForFilters();
        filterAdapter.setData(ingredients, "ingredient");
    }

    private void updateAdapterForFilters() {
        rvSearch.setVisibility(View.VISIBLE);
        rvSearch.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvSearch.setAdapter(filterAdapter);
    }

    public void navigateToFilterResults(String filterType, String filterValue) {
        int typeInt;

        switch (filterType) {
            case "category":
                typeInt = Constants.TYPE_CATEGORY;
                break;
            case "area":
                typeInt = Constants.TYPE_AREA;
                break;
            case "ingredient":
                typeInt = Constants.TYPE_INGREDIENT;
                break;
            default:
                typeInt = -1;
                break;
        }

        if (typeInt != -1) {
            SearchFragmentDirections.ActionSearchFragmentToMealsListFragment action =
                    SearchFragmentDirections.actionSearchFragmentToMealsListFragment(typeInt, filterValue);

            Navigation.findNavController(requireView()).navigate(action);
        }
    }

    @Override
    public void showError(String error) {
        rvSearch.setVisibility(View.GONE);
        AlertUtils.showErrorSnackBar(getView(), error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }



    public void navigateToDetails(Meal meal) {
        SearchFragmentDirections.ActionSearchFragmentToDetailsFragment action =
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }
}