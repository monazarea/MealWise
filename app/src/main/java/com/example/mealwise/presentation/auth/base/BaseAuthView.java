    package com.example.mealwise.presentation.auth.base;

    public interface BaseAuthView {
        void showButtonLoading();
        void hideLoading();
        void showError(String message);
        void showSuccess();
        void navigateToHome();
        void showEmailError(String message);
        void showPasswordError(String message);
    }
