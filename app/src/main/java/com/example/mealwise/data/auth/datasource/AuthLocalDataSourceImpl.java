package com.example.mealwise.data.auth.datasource;

import com.example.mealwise.data.auth.datasource.helpers.SharedPrefHelper;

public class AuthLocalDataSourceImpl implements AuthLocalDataSource {

    private final SharedPrefHelper sharedPrefHelper;

    public AuthLocalDataSourceImpl(SharedPrefHelper sharedPrefHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
    }

    @Override
    public void setGuestMode(boolean isGuest) {
        sharedPrefHelper.setGuestMode(isGuest);
    }

    @Override
    public boolean isGuestMode() {
        return sharedPrefHelper.isGuestMode();
    }

    @Override
    public void setFirstTimeLaunch(boolean isFirstTime) {
        sharedPrefHelper.setFirstTimeLaunch(isFirstTime);
    }

    @Override
    public boolean isFirstTimeLaunch() {
        return sharedPrefHelper.isFirstTimeLaunch();
    }

    @Override
    public void clearAuthData() {
        sharedPrefHelper.clearPreferences();
    }
}