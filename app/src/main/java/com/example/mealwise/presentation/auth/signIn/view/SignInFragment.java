package com.example.mealwise.presentation.auth.signIn.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mealwise.R;
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSourceImp;
import com.example.mealwise.data.auth.datasource.helpers.SharedPrefHelper;
import com.example.mealwise.data.auth.repository.AuthRepositoryImpl;
import com.example.mealwise.presentation.auth.base.BaseAuthFragment;
import com.example.mealwise.presentation.auth.signIn.presenter.SignInPresenter;
import com.example.mealwise.presentation.auth.signIn.presenter.SignInPresenterImpl;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInFragment extends BaseAuthFragment<SignInPresenter> implements SignInView {

    private TextInputLayout tlEmail, tlPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin, btnToggleSignUp;
    private LinearLayout btnGoogle;
    private TextView tvGuest, tvForgot;
    private ProgressBar progressBar;
    private String originalButtonText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    protected SignInPresenter createPresenter() {
        AuthRemoteDataSourceImp remoteDataSource = AuthRemoteDataSourceImp.getInstance();
        AuthRepositoryImpl repository = AuthRepositoryImpl.getInstance(remoteDataSource);
        return new SignInPresenterImpl(this, repository);
    }

    @Override
    protected void initViews(View view) {
        tlEmail = view.findViewById(R.id.tlEmail);
        tlPassword = view.findViewById(R.id.tlPassword);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnGoogle = view.findViewById(R.id.layoutGoogleSignIn);
        btnToggleSignUp = view.findViewById(R.id.btnToggleSignUp);
        tvGuest = view.findViewById(R.id.tvGuest);
        tvForgot = view.findViewById(R.id.tvForgot);
        progressBar = view.findViewById(R.id.progressBar);
        originalButtonText = btnLogin.getText().toString();
    }

    @Override
    protected void setupListeners() {
        clearErrorOnTyping(tlEmail, etEmail);
        clearErrorOnTyping(tlPassword, etPassword);

        btnLogin.setOnClickListener(v -> {
            clearErrors();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            presenter.signInWithEmailAndPassword(email, password);
        });

        btnGoogle.setOnClickListener(v -> signInWithGoogleFlow());

        btnToggleSignUp.setOnClickListener(v -> navigateToSignUp());

        tvGuest.setOnClickListener(v -> {
            SharedPrefHelper.getInstance(requireContext()).setGuestMode(true);
            navigateToHome();
        });
    }


    @Override
    public void showButtonLoading() {
        btnLogin.setEnabled(false);
        btnLogin.setText("");
        btnLogin.setAlpha(0.5f);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnLogin.setText(originalButtonText);
        btnLogin.setEnabled(true);
        btnLogin.setAlpha(1.0f);
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
    public void navigateToHome() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void navigateToSignUp() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_signUpFragment);
    }

    private void clearErrors() {
        tlEmail.setError(null);
        tlEmail.setErrorEnabled(false);
        tlPassword.setError(null);
        tlPassword.setErrorEnabled(false);
    }
}