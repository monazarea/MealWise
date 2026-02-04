package com.example.mealwise.data.auth.datasource.helpers;

import com.example.mealwise.data.auth.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Completable;

public class FirestoreHelper {

    private final FirebaseFirestore db;
    private static FirestoreHelper instance;

    private FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirestoreHelper getInstance() {
        if (instance == null) {
            instance = new FirestoreHelper();
        }
        return instance;
    }

    public Completable saveUser(User user) {
        return Completable.create(emitter -> {
            db.collection("users")
                    .document(user.getUserId())
                    .set(user)
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }
}