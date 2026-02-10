package com.example.mealwise.presentation.search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mealwise.data.auth.datasource.helpers.FirestoreHelper;
import com.example.mealwise.data.meals.datasource.local.MealsLocalDataSource;
import com.example.mealwise.data.meals.datasource.remote.MealsRemoteDataSource;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.repository.MealsRepositoryImpl;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.home.presenter.HomePresenterImpl;
import com.example.mealwise.presentation.home.view.MealsAdapter;
import com.example.mealwise.presentation.search.presenter.SearchPresenter;
import com.example.mealwise.presentation.search.presenter.SearchPresenterImpl;
import com.example.mealwise.utils.AlertUtils;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView {

    private SearchPresenter presenter;
    private MealsAdapter mealsAdapter;

    private RecyclerView rvSearch;
    private EditText etSearch;
    private ProgressBar progressBar;
    private ChipGroup chipGroup;
    private LinearLayout layoutNoResults;
    private ImageView btnBack;

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
        setupRecyclerView();
        presenter.prepareSearchObserver();
        setupSearchListener();
        if (etSearch.getText() != null && !etSearch.getText().toString().isEmpty()) {
            presenter.onSearchQueryChanged(etSearch.getText().toString());
        }
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

    }

    private void initViews(View view) {
        rvSearch = view.findViewById(R.id.rvSearch);
        etSearch = view.findViewById(R.id.etSearch);
        progressBar = view.findViewById(R.id.progressBarSearch);
        chipGroup = view.findViewById(R.id.chipGroupSearch);
        layoutNoResults = view.findViewById(R.id.layoutNoResults);
        btnBack = view.findViewById(R.id.btnBack);

    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rvSearch.setLayoutManager(gridLayoutManager);

        mealsAdapter = new MealsAdapter(requireContext(), new ArrayList<>(), this::navigateToDetails);
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
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        rvSearch.setVisibility(View.VISIBLE);
        layoutNoResults.setVisibility(View.GONE);
        mealsAdapter.setList(meals);
    }

    @Override
    public void showEmptyState() {
        rvSearch.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearResults() {
        mealsAdapter.setList(new ArrayList<>());
        rvSearch.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
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