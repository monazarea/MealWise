package com.example.mealwise.data.auth.repository;

import com.example.mealwise.data.auth.models.SignInRequest;
import com.example.mealwise.data.auth.models.SignUpRequest;
import com.example.mealwise.data.auth.models.User;

import io.reactivex.rxjava3.core.Completable;

public interface AuthRepository {
    Completable signUp(SignUpRequest signUPRequest);
    Completable signInWithGoogle(String idToken);
    Completable signIn(SignInRequest signInRequest);
    User getCurrentUser();
    Completable signOut();
}