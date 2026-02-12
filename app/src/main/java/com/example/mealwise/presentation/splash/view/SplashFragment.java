package com.example.mealwise.presentation.splash.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.mealwise.R;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.splash.presenter.SplashPresenter;
import com.example.mealwise.presentation.splash.presenter.SplashPresenterImpl;


public class SplashFragment extends Fragment implements  SplashView {


    private SplashPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        presenter = new SplashPresenterImpl(
                this,
//                Injection.provideSharedPrefHelper(requireContext()),
                Injection.provideAuthRepository(requireContext())
        );
        presenter.checkNavigationLogic();
    }

    @Override
    public void navigateToHome() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_splashFragment_to_homeFragment);
    }

    @Override
    public void navigateToSignIn() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_splashFragment_to_loginFragment);
    }

    @Override
    public void navigateToOnboarding() {

        navigateToSignIn();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}