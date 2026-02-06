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
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSourceImp;
import com.example.mealwise.data.auth.datasource.helpers.SharedPrefHelper;
import com.example.mealwise.data.auth.repository.AuthRepositoryImpl;
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

        SharedPrefHelper prefManager = SharedPrefHelper.getInstance(requireContext());
        AuthRemoteDataSourceImp remoteDataSource = AuthRemoteDataSourceImp.getInstance();
        AuthRepositoryImpl repository = AuthRepositoryImpl.getInstance(remoteDataSource);

        presenter = new SplashPresenterImpl(this, prefManager,repository);

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
        // todo i will change after add onBoarding screens
        navigateToSignIn();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}