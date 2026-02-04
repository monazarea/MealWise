package com.example.mealwise.data.auth.datasource;

import com.example.mealwise.data.auth.models.SignUpRequest;
import io.reactivex.rxjava3.core.Completable;

public interface AuthRemoteDataSource {
    Completable signUp(SignUpRequest signUPRequest);
}