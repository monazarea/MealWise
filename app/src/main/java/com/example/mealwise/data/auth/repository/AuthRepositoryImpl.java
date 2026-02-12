package com.example.mealwise.data.auth.repository;

import com.example.mealwise.data.auth.datasource.AuthLocalDataSource;
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSource;
import com.example.mealwise.data.auth.models.SignInRequest;
import com.example.mealwise.data.auth.models.SignUpRequest;
import com.example.mealwise.data.auth.models.User;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthLocalDataSource localDataSource;
    private final AuthRemoteDataSource remoteDataSource;
    private static AuthRepositoryImpl instance;

    private AuthRepositoryImpl(AuthRemoteDataSource remoteDataSource,AuthLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource =localDataSource;
    }

    public static AuthRepositoryImpl getInstance(AuthRemoteDataSource remoteDataSource,AuthLocalDataSource localDataSource) {
        if (instance == null) {
            instance = new AuthRepositoryImpl(remoteDataSource,localDataSource);
        }
        return instance;
    }

    @Override
    public Completable signUp(SignUpRequest signUPRequest) {
        return remoteDataSource.signUp(signUPRequest);
    }

    @Override
    public Completable signInWithGoogle(String idToken) {
        return remoteDataSource.googleSignIn(idToken);
    }

    @Override
    public Completable signIn(SignInRequest signInRequest) {
        return remoteDataSource.signIn(signInRequest);
    }
    public User getCurrentUser() {
        FirebaseUser firebaseUser = remoteDataSource.getCurrentUser();

        if (firebaseUser != null) {
            return new User(
                    firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail()
            );
        }
        return null;
    }

    @Override
    public Completable signOut() {
        return remoteDataSource.signOut();
    }





    @Override
    public void setGuestMode(boolean isGuest) {
        localDataSource.setGuestMode(isGuest);
    }

    @Override
    public boolean isGuestMode() {
        return localDataSource.isGuestMode();
    }

    @Override
    public void setFirstTimeLaunch(boolean isFirstTime) {
        localDataSource.setFirstTimeLaunch(isFirstTime);
    }

    @Override
    public boolean isFirstTimeLaunch() {
        return localDataSource.isFirstTimeLaunch();
    }

    @Override
    public void clearAuthData() {
        localDataSource.clearAuthData();
    }
}