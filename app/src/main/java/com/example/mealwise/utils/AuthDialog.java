package com.example.mealwise.utils;


import android.content.Context;
import androidx.navigation.NavController;
import com.example.mealwise.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AuthDialog {

    public static void showGuestFavoriteDialog(Context context, NavController navController) {
        new MaterialAlertDialogBuilder(context, R.style.CustomAlertDialogTheme)
                .setTitle("Login Required")
                .setMessage("This feature is only available for registered users. Would you like to login or sign up now?")
                .setPositiveButton("Go to Login", (dialog, which) -> {
                    navController.navigate(R.id.action_global_loginFragment);
                })
                .setNegativeButton("Stay as Guest", null)
                .show();
    }
}
