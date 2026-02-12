package com.example.mealwise.presentation.profile.view;

public interface ProfileView {
    void showUserData(String name, String email);
    void showGuestMode();
    void navigateToLogin();
    void showError(String message);
}