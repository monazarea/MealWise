package com.example.mealwise.presentation.auth.signUp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.mealwise.R;
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSourceImp;
import com.example.mealwise.data.auth.repository.AuthRepositoryImpl;
import com.example.mealwise.presentation.auth.signUp.presenter.SignUpPresenter;
import com.example.mealwise.presentation.auth.signUp.presenter.SignUpPresenterImpl;
import com.example.mealwise.utils.AlertUtils;
import com.example.mealwise.utils.GoogleAuthManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.credentials.CredentialManager;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class SignUpFragment extends Fragment implements SignUpView {

    private SignUpPresenter presenter;
    private TextInputLayout tlUsername, tlEmail, tlPassword, tlConfirmPassword;
    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private String originalButtonText;
    private LinearLayout btnGoogle;
    private GoogleAuthManager googleAuthManager;
    private final CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        clearErrorOnTyping(tlUsername, etUsername);
        clearErrorOnTyping(tlEmail, etEmail);
        clearErrorOnTyping(tlPassword, etPassword);
        clearErrorOnTyping(tlConfirmPassword, etConfirmPassword);

        AuthRemoteDataSourceImp remoteDataSource = AuthRemoteDataSourceImp.getInstance();
        AuthRepositoryImpl repository = AuthRepositoryImpl.getInstance(remoteDataSource);
        presenter = new SignUpPresenterImpl(this, repository);

        String webClientId = getString(R.string.default_web_client_id);
        googleAuthManager = new GoogleAuthManager(requireContext(), webClientId);
        btnSignUp.setOnClickListener(v -> {
            clearErrors();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            presenter.registerWithEmailAndPassword(username, email, password, confirmPass);
        });
        btnGoogle.setOnClickListener(v -> {
            signInWithGoogle();
        });
    }
    private void signInWithGoogle() {
        disposables.add(
                googleAuthManager.signIn()
                        .subscribe(
                                idToken -> {
                                    presenter.registerWithGoogle(idToken);
                                },
                                throwable -> {
                                    hideLoading();
                                    showError(throwable.getMessage());
                                }
                        )
        );
    }
    private void initViews(View view) {

        tlUsername = view.findViewById(R.id.tlUserName);
        tlEmail = view.findViewById(R.id.tlEmail1);
        tlPassword = view.findViewById(R.id.tlPassword1);
        tlConfirmPassword = view.findViewById(R.id.tlConfirmPassword1);

        etUsername = view.findViewById(R.id.etUserName);
        etEmail = view.findViewById(R.id.etEmail1);
        etPassword = view.findViewById(R.id.etPassword1);
        etConfirmPassword = view.findViewById(R.id.etConfiremPassword1);
        btnSignUp = view.findViewById(R.id.btnSignUP);
        progressBar = view.findViewById(R.id.progressBar);
        originalButtonText = btnSignUp.getText().toString();
        btnGoogle = view.findViewById(R.id.layoutGoogleSignIn);
    }

    @Override
    public void showButtonLoading() {
        btnSignUp.setEnabled(false);
        btnSignUp.setText("");
        btnSignUp.setAlpha(0.5f);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnSignUp.setText(originalButtonText);
        btnSignUp.setEnabled(true);
        btnSignUp.setAlpha(1.0f);
    }

    @Override
    public void showNameError(String message) {
        tlUsername.setError(message);
        tlUsername.requestFocus();
    }

    @Override
    public void showEmailError(String message) {
        tlEmail.setError(message);
        tlEmail.requestFocus();
    }

    @Override
    public void showPasswordError(String message) {
        tlPassword.setError(message);
        tlPassword.requestFocus();
    }

    @Override
    public void showConfirmPasswordError(String message) {
        tlConfirmPassword.setError(message);
        tlConfirmPassword.requestFocus();
    }

    @Override
    public void showError(String message) {
        AlertUtils.showErrorSnackBar(getView(), message);    }

    @Override
    public void showSuccess() {
        AlertUtils.showSuccessSnackBar(getView(), "Account Created Successfully!");    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        disposables.clear();
    }
    private void clearErrors() {
        tlUsername.setError(null);
        tlUsername.setErrorEnabled(false);

        tlEmail.setError(null);
        tlEmail.setErrorEnabled(false);

        tlPassword.setError(null);
        tlPassword.setErrorEnabled(false);

        tlConfirmPassword.setError(null);
        tlConfirmPassword.setErrorEnabled(false);

    }

    private void clearErrorOnTyping(TextInputLayout layout, TextInputEditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layout.getError() != null) {
                    layout.setError(null);
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });
    }


}