package com.example.mealwise.data.auth.datasource;

import com.example.mealwise.data.auth.models.SignUpRequest;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRemoteDataSource {
    Completable signUp(SignUpRequest signUPRequest);
    Completable googleSignIn(String idToken);
}