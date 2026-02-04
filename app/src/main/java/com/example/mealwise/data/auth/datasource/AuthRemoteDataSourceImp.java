package com.example.mealwise.data.auth.datasource;

import com.example.mealwise.data.auth.datasource.helpers.FirebaseHelper;
import com.example.mealwise.data.auth.datasource.helpers.FirestoreHelper;
import com.example.mealwise.data.auth.models.SignUpRequest;
import com.example.mealwise.data.auth.models.User;

import io.reactivex.rxjava3.core.Completable;

public class AuthRemoteDataSourceImp implements AuthRemoteDataSource {

    private final FirebaseHelper authHelper;
    private final FirestoreHelper firestoreHelper;
    private static AuthRemoteDataSourceImp instance;

    private AuthRemoteDataSourceImp() {
        authHelper = FirebaseHelper.getInstance();
        firestoreHelper = FirestoreHelper.getInstance();
    }

    public static AuthRemoteDataSourceImp getInstance() {
        if (instance == null) {
            instance = new AuthRemoteDataSourceImp();
        }
        return instance;
    }

    @Override
    public Completable signUp(SignUpRequest signUPRequest) {

        return authHelper.signUp(signUPRequest)

                .flatMapCompletable(firebaseUser -> {

                    User newUser = new User(firebaseUser.getUid(), signUPRequest.getUsername(), firebaseUser.getEmail());

                    return authHelper.updateUserName(firebaseUser, signUPRequest.getUsername())
                            .andThen(firestoreHelper.saveUser(newUser));
                });
    }
}