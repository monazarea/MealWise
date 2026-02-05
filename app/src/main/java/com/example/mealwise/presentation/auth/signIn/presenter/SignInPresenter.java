package com.example.mealwise.presentation.auth.signIn.presenter;

import com.example.mealwise.presentation.auth.base.BaseAuthPresenter;

public interface SignInPresenter extends BaseAuthPresenter {
    void signInWithEmailAndPassword(String email, String password);

}
