package com.example.mealwise.presentation.mealslist.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.home.view.MealsAdapter;
import com.example.mealwise.presentation.mealslist.presenter.MealsListPresenterImpl;
import com.example.mealwise.utils.AlertUtils;

import java.util.List;

public class MealsListFragment extends Fragment implements MealsListView, MealsAdapter.OnMealClickListener {

    private MealsListPresenterImpl presenter;
    private RecyclerView rvMeals;
    private ProgressBar progressBar;
    private TextView tvPageTitle;
    private TextView tvNoResult;
    private EditText etSearch;
    private MealsAdapter mealsAdapter;
    private ImageView btnBack;

    private int type;
    private String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            MealsListFragmentArgs args = MealsListFragmentArgs.fromBundle(getArguments());
            type = args.getType();
            name = args.getName();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meals_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        if (name != null && !name.isEmpty()) {
            tvPageTitle.setText(name + " Meals");
        }

        presenter = new MealsListPresenterImpl(this, Injection.provideMealsRepository(requireContext()));

        presenter.getMeals(type, name);

        btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void initViews(View view) {
        rvMeals = view.findViewById(R.id.rvMeals);
        progressBar = view.findViewById(R.id.progressBar);
        tvPageTitle = view.findViewById(R.id.tvPageTitle);
        tvNoResult = view.findViewById(R.id.tvNoResult);
        etSearch = view.findViewById(R.id.etSearch);
        btnBack = view.findViewById(R.id.btnBack);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rvMeals.setLayoutManager(gridLayoutManager);
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvMeals.setVisibility(View.GONE);
        tvNoResult.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        rvMeals.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            mealsAdapter = new MealsAdapter(requireContext(), meals, this);
            rvMeals.setAdapter(mealsAdapter);
            tvNoResult.setVisibility(View.GONE);
        } else {
            rvMeals.setVisibility(View.GONE);
            tvNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        AlertUtils.showErrorSnackBar(getView(), message) ;   }


    @Override
    public void onMealClick(Meal meal) {

        MealsListFragmentDirections.ActionMealsListFragmentToDetailsFragment action =
                MealsListFragmentDirections.actionMealsListFragmentToDetailsFragment(meal);

        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}