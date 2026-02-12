package com.example.mealwise.presentation.profile.presenter;

import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.presentation.profile.view.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {

    private ProfileView view;
    private AuthRepository authRepository;

    public ProfilePresenterImpl(ProfileView view, AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void getProfileData() {
        if (authRepository.isGuestMode()) {
            view.showGuestMode();
        } else {
            String name = authRepository.getCurrentUser().getUserName() != null ? authRepository.getCurrentUser().getUserName() : "MealWise User";
            String email = authRepository.getCurrentUser().getEmail() != null ? authRepository.getCurrentUser().getEmail() : "No Email Provided";

            view.showUserData(name, email);
        }
    }

    @Override
    public void handleAction() {
        if (authRepository.isGuestMode()) {
            view.navigateToLogin();
        } else {
            authRepository.clearAuthData();
            view.navigateToLogin();
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}