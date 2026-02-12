package com.example.mealwise.presentation.auth.signUp.presenter;

import com.example.mealwise.presentation.auth.base.BaseAuthPresenter;

public interface SignUpPresenter extends BaseAuthPresenter {
    void registerWithEmailAndPassword(String username, String email, String password, String confirmPassword);

}
