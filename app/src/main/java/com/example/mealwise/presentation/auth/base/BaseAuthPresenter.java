package com.example.mealwise.presentation.auth.base;

public interface BaseAuthPresenter {
    void signInWithGoogle(String idToken);
    void onDestroy();

}
