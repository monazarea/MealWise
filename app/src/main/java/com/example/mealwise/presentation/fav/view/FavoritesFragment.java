package com.example.mealwise.presentation.fav.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealwise.R;
import com.example.mealwise.data.meals.datasource.local.MealsLocalDataSource;
import com.example.mealwise.data.meals.datasource.remote.MealsRemoteDataSource;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.data.meals.repository.MealsRepositoryImpl;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.details.view.DetailsFragment;
import com.example.mealwise.presentation.fav.presneter.FavoritesPresenter;
import com.example.mealwise.presentation.fav.presneter.FavoritesPresenterImpl;
import com.example.mealwise.presentation.home.presenter.HomePresenterImpl;
import com.example.mealwise.presentation.home.view.HomeFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesView, FavoriteMealsAdapter.OnFavoriteClickListener {

    private RecyclerView recyclerView;
    private FavoriteMealsAdapter adapter;
    private FavoritesPresenter presenter;
    private Group emptyStateGroup;
    private ImageView btnBack;


    public FavoritesFragment() {  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new FavoritesPresenterImpl(this, Injection.provideMealsRepository(requireContext())) {};

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvFavorites);
        emptyStateGroup = view.findViewById(R.id.groupEmptyState);

        adapter = new FavoriteMealsAdapter(requireContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        btnBack = view.findViewById(R.id.btnBack);

        presenter.getFavorites();
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

    }


    @Override
    public void showFavorites(List<Meal> meals) {
        emptyStateGroup.setVisibility(View.GONE);
        adapter.setList(meals);
    }

    @Override
    public void showEmptyState() {
        adapter.setList(new ArrayList<>());
        emptyStateGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRemoveSuccess(Meal meal) {
        Toast.makeText(requireContext(), meal.getName() + " removed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRemoveClick(Meal meal) {
        presenter.removeFromFavorites(meal);
    }

    @Override
    public void onItemClick(Meal meal) {

        navigateToDetails(meal);
    }
    public void navigateToDetails(Meal meal) {
        FavoritesFragmentDirections.ActionFavoritesFragment3ToDetailsFragment action =
        FavoritesFragmentDirections.actionFavoritesFragment3ToDetailsFragment(meal);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}