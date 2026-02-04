package com.example.mealwise.presentation.auth.signUp.view;

public interface SignUpView {
    void showButtonLoading();
    void hideLoading();

    void showNameError(String message);
    void showEmailError(String message);
    void showPasswordError(String message);
    void showConfirmPasswordError(String message);

    void showError(String message);
    void showSuccess();
    void navigateToHome();

}
