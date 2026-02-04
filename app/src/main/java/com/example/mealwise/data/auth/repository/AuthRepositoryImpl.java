package com.example.mealwise.data.auth.repository;

import com.example.mealwise.data.auth.datasource.AuthRemoteDataSource;
import com.example.mealwise.data.auth.models.SignUpRequest;

import io.reactivex.rxjava3.core.Completable;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthRemoteDataSource remoteDataSource;
    private static AuthRepositoryImpl instance;

    private AuthRepositoryImpl(AuthRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static AuthRepositoryImpl getInstance(AuthRemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new AuthRepositoryImpl(remoteDataSource);
        }
        return instance;
    }

    @Override
    public Completable signUp(SignUpRequest signUPRequest) {
        return remoteDataSource.signUp(signUPRequest);
    }
}