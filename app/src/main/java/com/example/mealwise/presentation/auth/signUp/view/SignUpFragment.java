package com.example.mealwise.presentation.auth.signUp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.mealwise.R;
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSourceImp;
import com.example.mealwise.data.auth.datasource.helpers.SharedPrefHelper;
import com.example.mealwise.data.auth.repository.AuthRepositoryImpl;
import com.example.mealwise.presentation.auth.base.BaseAuthFragment;
import com.example.mealwise.presentation.auth.signUp.presenter.SignUpPresenter;
import com.example.mealwise.presentation.auth.signUp.presenter.SignUpPresenterImpl;
import com.example.mealwise.utils.AlertUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends BaseAuthFragment<SignUpPresenter> implements SignUpView {

    private TextInputLayout tlUsername, tlEmail, tlPassword, tlConfirmPassword;
    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp, btnToggleSignIn;
    private ProgressBar progressBar;
    private LinearLayout btnGoogle;
    private String originalButtonText;
    private TextView tvGuest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    protected SignUpPresenter createPresenter() {
        AuthRemoteDataSourceImp remoteDataSource = AuthRemoteDataSourceImp.getInstance();
        AuthRepositoryImpl repository = AuthRepositoryImpl.getInstance(remoteDataSource);
        return new SignUpPresenterImpl(this, repository);
    }

    @Override
    protected void initViews(View view) {
        tlUsername = view.findViewById(R.id.tlUserName);
        tlEmail = view.findViewById(R.id.tlEmail1);
        tlPassword = view.findViewById(R.id.tlPassword1);
        tlConfirmPassword = view.findViewById(R.id.tlConfirmPassword1);
        etUsername = view.findViewById(R.id.etUserName);
        etEmail = view.findViewById(R.id.etEmail1);
        etPassword = view.findViewById(R.id.etPassword1);
        etConfirmPassword = view.findViewById(R.id.etConfiremPassword1);
        btnSignUp = view.findViewById(R.id.btnSignUP);
        btnToggleSignIn = view.findViewById(R.id.btnToggleLogin);
        btnGoogle = view.findViewById(R.id.layoutGoogleSignIn);
        progressBar = view.findViewById(R.id.progressBar);
        originalButtonText = btnSignUp.getText().toString();
        tvGuest = view.findViewById(R.id.tvGuest);
        btnToggleSignIn = view.findViewById(R.id.btnToggleLogin1);

    }

    @Override
    protected void setupListeners() {
        clearErrorOnTyping(tlUsername, etUsername);
        clearErrorOnTyping(tlEmail, etEmail);
        clearErrorOnTyping(tlPassword, etPassword);
        clearErrorOnTyping(tlConfirmPassword, etConfirmPassword);

        btnSignUp.setOnClickListener(v -> {
            clearErrors();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();
            presenter.registerWithEmailAndPassword(username, email, password, confirmPass);
        });

        btnGoogle.setOnClickListener(v -> signInWithGoogleFlow());

       btnToggleSignIn.setOnClickListener(v -> navigateToSignIn());
        tvGuest.setOnClickListener(v -> {
            SharedPrefHelper.getInstance(requireContext()).setGuestMode(true);
            navigateToHome();
        });
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
    public void showSuccess() {
        AlertUtils.showSuccessSnackBar(getView(), "Account Created Successfully!");
    }

    @Override
    public void navigateToSignIn() {
        Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_loginFragment);
    }

    @Override
    public void navigateToHome() {
        SharedPrefHelper.getInstance(requireContext()).setGuestMode(false);
        Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_homeFragment);
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
}