package com.example.mealwise.presentation.profile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.mealwise.R;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.profile.presenter.ProfilePresenter;
import com.example.mealwise.presentation.profile.presenter.ProfilePresenterImpl;

public class ProfileFragment extends Fragment implements ProfileView {

    private ProfilePresenter presenter;
    private TextView tvUserName, tvUserEmail;
    private Button btnProfileAction;
    private ImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnProfileAction = view.findViewById(R.id.btnProfileAction);
        btnBack = view.findViewById(R.id.btnBack);

        presenter = new ProfilePresenterImpl(this, Injection.provideAuthRepository(requireContext()));

        presenter.getProfileData();

        btnProfileAction.setOnClickListener(v -> presenter.handleAction());

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    @Override
    public void showUserData(String name, String email) {
        tvUserName.setText(name);
        tvUserEmail.setText(email);
        tvUserEmail.setVisibility(View.VISIBLE);
        btnProfileAction.setText("Logout");
    }

    @Override
    public void showGuestMode() {
        tvUserName.setText("Guest User");
        tvUserEmail.setVisibility(View.GONE);
        btnProfileAction.setText("Login / Sign Up");
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_global_loginFragment);
    }

    @Override
    public void showError(String message) {
    }
}