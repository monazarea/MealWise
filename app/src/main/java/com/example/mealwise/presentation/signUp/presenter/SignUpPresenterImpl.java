package com.example.mealwise.presentation.signUp.presenter;

import android.text.TextUtils;

import com.example.mealwise.data.auth.models.SignUpRequest;
import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.presentation.signUp.view.SignUpView;
import com.example.mealwise.utils.ValidationUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpPresenterImpl implements SignUpPresenter{

    private final SignUpView view;
    private final AuthRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SignUpPresenterImpl(SignUpView view, AuthRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    @Override
    public void registerUser(String username, String email, String password, String confirmPassword) {
        if(!validateAllFields(username, email, password, confirmPassword))
            return;

        SignUpRequest request = new SignUpRequest(username, email, password);
        view.showLoading();

        performSignUp(request);
    }

    private void performSignUp(SignUpRequest request){
        disposables.add(
                repository.signUp(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.showSuccess();
                                    view.navigateToHome();
                                },
                                error -> {
                                    view.hideLoading();
                                    view.showError(error.getMessage());
                                }
                        )
        );
    }


    private boolean validateAllFields(String username, String email, String password, String confirmPassword) {
        boolean isValid = true;

        String nameError = ValidationUtils.getUsernameError(username);
        if (nameError != null) {
            view.showNameError(nameError);
            isValid = false;
        }

        String emailError = ValidationUtils.getEmailError(email);
        if (emailError != null) {
            view.showEmailError(emailError);
            isValid = false;
        }

        String passwordError = ValidationUtils.getPasswordError(password);
        if (passwordError != null) {
            view.showPasswordError(passwordError);
            isValid = false;
        }

        String confirmError = ValidationUtils.getConfirmPasswordError(password, confirmPassword);
        if (confirmError != null) {
            view.showConfirmPasswordError(confirmError);
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void continueAsGuest() {

    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
