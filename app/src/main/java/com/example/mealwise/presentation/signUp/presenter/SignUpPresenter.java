package com.example.mealwise.presentation.signUp.presenter;

public interface SignUpPresenter {
    void registerUser(String username, String email, String password, String confirmPassword);
    void continueAsGuest();

    void onDestroy();
}
