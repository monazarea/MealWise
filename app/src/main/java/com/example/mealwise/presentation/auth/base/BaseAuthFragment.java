package com.example.mealwise.presentation.auth.base;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mealwise.R;
import com.example.mealwise.utils.AlertUtils;
import com.example.mealwise.utils.GoogleAuthManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BaseAuthFragment<P extends BaseAuthPresenter> extends Fragment implements BaseAuthView {

    protected P presenter;
    protected GoogleAuthManager googleAuthManager;
    protected final CompositeDisposable disposables = new CompositeDisposable();

    protected abstract P createPresenter();
    protected abstract void initViews(View view);
    protected abstract void setupListeners();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        presenter = createPresenter();

        String webClientId = getString(R.string.default_web_client_id);
        googleAuthManager = new GoogleAuthManager(requireContext(), webClientId);

        setupListeners();
    }

    protected void signInWithGoogleFlow() {
        disposables.add(
                googleAuthManager.signIn()
                        .subscribe(
                                idToken -> presenter.signInWithGoogle(idToken),
                                throwable -> {
                                    hideLoading();
                                    showError(throwable.getMessage());
                                }
                        )
        );
    }

    protected void clearErrorOnTyping(TextInputLayout layout, TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layout.getError() != null) {
                    layout.setError(null);
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    @Override
    public void showError(String message) {
        AlertUtils.showErrorSnackBar(getView(), message);
    }

    public void showSuccess() {
        AlertUtils.showSuccessSnackBar(getView(), "Success!");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
        disposables.clear();
    }
}
