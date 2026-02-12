package com.example.mealwise.presentation.fav.presneter;

import com.example.mealwise.data.meals.models.Meal;

public interface FavoritesPresenter {
    void getFavorites();
    void removeFromFavorites(Meal meal);
    Boolean isGuestMode();
    void onDestroy();
}

