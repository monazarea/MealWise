package com.example.mealwise.presentation.home.presenter;

public interface HomePresenter {
    public void getHomeData();
    public void getMealsByCategory(String categoryName);
    public void onDestroy();
}
