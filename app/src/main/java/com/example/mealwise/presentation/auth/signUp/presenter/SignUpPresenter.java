package com.example.mealwise.presentation.auth.signUp.presenter;

public interface SignUpPresenter {
    void registerWithEmailAndPassword(String username, String email, String password, String confirmPassword);
    void continueAsGuest();
    void registerWithGoogle(String idToken);


    void onDestroy();
}
