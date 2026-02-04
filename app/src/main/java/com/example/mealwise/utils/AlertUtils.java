package com.example.mealwise.utils;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.example.mealwise.R;

public class AlertUtils {


    public static void showSuccessSnackBar(View view, String message) {
        if (view == null) return;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.more_light_green));
        snackbar.setTextColor(ContextCompat.getColor(view.getContext(), R.color.success_green));

        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(null, Typeface.BOLD);
        snackbar.show();
    }

    public static void showErrorSnackBar(View view, String message) {
        if (view == null) return;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.more_light_green));
        snackbar.setTextColor(ContextCompat.getColor(view.getContext(), R.color.error_red));

        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(null, Typeface.BOLD);
        snackbar.setAction("Dismiss", v -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.background_dark));

        snackbar.show();
    }
}