package com.example.mealwise.presentation.main;


import android.app.Application;
import android.util.Log;

import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class MealWiseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        configureRxJavaErrorHandler();
    }

    private void configureRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof java.io.IOException) || (e instanceof java.net.SocketException)) {
                return;
            }
            if (e instanceof InterruptedException) {
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }

            Log.w("MealWiseApp", "Undeliverable exception received, not sure what to do", e);
        });
    }
}
