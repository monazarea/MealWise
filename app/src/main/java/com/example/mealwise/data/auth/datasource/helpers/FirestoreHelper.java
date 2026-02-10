package com.example.mealwise.data.auth.datasource.helpers;

import com.example.mealwise.data.auth.models.User;
import com.example.mealwise.data.meals.models.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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
                    .addOnSuccessListener(aVoid -> {
                        if(!emitter.isDisposed()){
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e->{
                        if(!emitter.isDisposed()){
                            emitter.onError(e);
                        }
                    });
        });
    }


    public Completable insertMeal(Meal meal) {
        return Completable.create(emitter -> {

            String userId = meal.getUserId();
            if (userId == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
            String documentId;
            if ("PLAN".equals(meal.getType())) {
                documentId = meal.getId() + "_" + meal.getDayOfWeek();
            } else {
                documentId = meal.getId();
            }
            db.collection("users")
                    .document(userId)
                    .collection("meals")
                    .document(documentId)
                    .set(meal)
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Completable deleteMeal(Meal meal) {
        return Completable.create(emitter -> {
            String userId = meal.getUserId();
            if (userId == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

            String documentId;
            if ("PLAN".equals(meal.getType())) {
                documentId = meal.getId() + "_" + meal.getDayOfWeek();
            } else {
                documentId = meal.getId();
            }

            db.collection("users")
                    .document(userId)
                    .collection("meals")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Single<List<Meal>> getMeals(String userId) {
        return Single.create(emitter -> {
            db.collection("users")
                    .document(userId)
                    .collection("meals")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!emitter.isDisposed()) {
                            List<Meal> meals = queryDocumentSnapshots.toObjects(Meal.class);
                            emitter.onSuccess(meals);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }
}