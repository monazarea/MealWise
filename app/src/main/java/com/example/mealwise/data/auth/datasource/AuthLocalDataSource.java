package com.example.mealwise.data.auth.datasource;

public interface AuthLocalDataSource {
    void setGuestMode(boolean isGuest);
    boolean isGuestMode();
    void setFirstTimeLaunch(boolean isFirstTime);
    boolean isFirstTimeLaunch();
    void clearAuthData();
}