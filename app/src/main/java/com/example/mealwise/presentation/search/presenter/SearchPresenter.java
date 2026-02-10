package com.example.mealwise.presentation.search.presenter;

public interface SearchPresenter {
    void onSearchQueryChanged(String query);
    void onDestroy();
    void prepareSearchObserver();
}
