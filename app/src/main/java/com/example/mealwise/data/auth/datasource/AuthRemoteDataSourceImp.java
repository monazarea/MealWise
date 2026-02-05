package com.example.mealwise.data.auth.datasource;

import com.example.mealwise.data.auth.datasource.helpers.FirebaseHelper;
import com.example.mealwise.data.auth.datasource.helpers.FirestoreHelper;
import com.example.mealwise.data.auth.models.SignInRequest;
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

    @Override
    public Completable googleSignIn(String idToken) {
        return authHelper.signInWithGoogle(idToken)
                .flatMapCompletable(firebaseUser ->{
                    User user = new User(
                            firebaseUser.getUid(),
                            firebaseUser.getDisplayName(),
                            firebaseUser.getEmail()
                    );
                    return firestoreHelper.saveUser(user);
                });
    }

    @Override
    public Completable signIn(SignInRequest signInRequest) {
        return authHelper.signIn(signInRequest)
                .ignoreElement();
    }




}