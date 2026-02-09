package com.example.mealwise.data.auth.datasource.helpers;

import com.example.mealwise.data.auth.models.SignInRequest;
import com.example.mealwise.data.auth.models.SignUpRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseHelper {

    private final FirebaseAuth firebaseAuth;
    private static FirebaseHelper instance;

    private FirebaseHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public Single<FirebaseUser> signUp(SignUpRequest signUPRequest) {
        return Single.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(signUPRequest.getEmail(), signUPRequest.getPassword())
                    .addOnSuccessListener(authResult -> {
                        if(!emitter.isDisposed()){

                            if (authResult.getUser() != null) {
                                emitter.onSuccess(authResult.getUser());
                            } else {
                                emitter.onError(new Exception("User is null"));
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }
    public Single<FirebaseUser> signIn(SignInRequest loginRequest) {
        return Single.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                    .addOnSuccessListener(authResult -> {
                        if (!emitter.isDisposed()) {
                            if (authResult.getUser() != null) {
                                emitter.onSuccess(authResult.getUser());
                            } else {
                                emitter.onError(new Exception("User is null"));
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Single<FirebaseUser> signInWithGoogle(String idToken) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

            firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        if (authResult.getUser() != null) {
                            emitter.onSuccess(authResult.getUser());
                        } else {
                            emitter.onError(new Exception("Google Sign In failed: User is null"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Completable updateUserName(FirebaseUser user, String username) {
        return Completable.create(emitter -> {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public FirebaseUser getCurrentUser() {

        return firebaseAuth.getCurrentUser();
    }
}