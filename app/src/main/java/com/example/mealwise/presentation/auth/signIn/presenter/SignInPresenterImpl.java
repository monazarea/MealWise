package com.example.mealwise.presentation.auth.signIn.presenter;

import com.example.mealwise.data.auth.models.SignInRequest;
import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.presentation.auth.base.BaseAuthPresenterImpl;
import com.example.mealwise.presentation.auth.signIn.view.SignInView;
import com.example.mealwise.utils.ValidationUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignInPresenterImpl extends BaseAuthPresenterImpl<SignInView> implements SignInPresenter {

    public SignInPresenterImpl(SignInView view, AuthRepository repository) {
        super(view, repository);
    }
    @Override
    public void signInWithEmailAndPassword(String email, String password) {
        if (!validateAllFields(email, password))
            return;

        SignInRequest request = new SignInRequest(email, password);
        view.showButtonLoading();

        performSignInWithEmailAndPassword(request);
    }


    private void performSignInWithEmailAndPassword(SignInRequest request) {
        disposables.add(
                repository.signIn(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.navigateToHome();
                                },
                                error -> {
                                    view.hideLoading();
                                    handleError(error);
                                }
                        )
        );
    }


    private boolean validateAllFields(String email, String password) {
        boolean isValid = true;

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

        return isValid;
    }

}