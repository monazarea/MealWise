package com.example.mealwise.utils;


import android.content.Context;

import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Meal;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DeleteDialogUtil {

    public interface OnConfirmationListener {
        void onConfirm();
    }

    public static void showDeleteConfirmation(Context context, String mealName, OnConfirmationListener listener) {
        new MaterialAlertDialogBuilder(context, R.style.CustomAlertDialogTheme)
                .setTitle("Remove from Plan")
                .setMessage("Are you sure you want to remove \"" + mealName + "\" from your weekly planner?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}