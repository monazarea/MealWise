package com.example.mealwise.presentation.main;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealwise.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private View navDivider;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            NavigationUI.setupWithNavController(bottomNav, navController);

            setupNavVisibility();
        }

    }

    private void setupNavVisibility() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.homeFragment
                    || destination.getId() == R.id.favoritesFragment3
                    || destination.getId() == R.id.profileFragment2) {

                bottomNav.setVisibility(View.VISIBLE);
                navDivider.setVisibility(View.VISIBLE);
            } else {
                bottomNav.setVisibility(View.GONE);
                navDivider.setVisibility(View.GONE);
            }
        });
    }
    private void initViews() {
        bottomNav = findViewById(R.id.bottomNav);
        navDivider = findViewById(R.id.navDivider);
    }
}