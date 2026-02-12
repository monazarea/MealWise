package com.example.mealwise.data.auth.datasource;

import com.example.mealwise.data.auth.models.SignInRequest;
import com.example.mealwise.data.auth.models.SignUpRequest;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public interface AuthRemoteDataSource {
    Completable signUp(SignUpRequest signUPRequest);
    Completable googleSignIn(String idToken);
    Completable signIn(SignInRequest signInRequest);

    FirebaseUser getCurrentUser();
    Completable signOut();
}