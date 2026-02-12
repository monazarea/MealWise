package com.example.mealwise.presentation.auth.signUp.view;

import com.example.mealwise.presentation.auth.base.BaseAuthView;

public interface SignUpView extends BaseAuthView {

    void showNameError(String message);

    void showConfirmPasswordError(String message);
    void navigateToSignIn();
//    void showError(String message);
//    void showSuccess();
//    void navigateToHome();

}
