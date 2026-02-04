package com.example.mealwise.data.auth.repository;

import com.example.mealwise.data.auth.models.SignUpRequest;
import io.reactivex.rxjava3.core.Completable;

public interface AuthRepository {
    Completable signUp(SignUpRequest signUPRequest);
}