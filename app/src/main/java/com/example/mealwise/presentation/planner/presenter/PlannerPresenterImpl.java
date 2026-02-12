package com.example.mealwise.presentation.planner.presenter;

import android.util.Log;

import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.planner.view.PlannerView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenterImpl implements PlannerPresenter {

    private PlannerView view;
    private MealsRepository repository;
    private AuthRepository authRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private io.reactivex.rxjava3.disposables.Disposable currentDaySubscription;

    public PlannerPresenterImpl(PlannerView view, MealsRepository repository, AuthRepository authRepository) {
        this.view = view;
        this.repository = repository;
        this.authRepository = authRepository;
    }

    @Override
    public void getPlannedByDay(String day) {

        if (currentDaySubscription != null && !currentDaySubscription.isDisposed()) {
            currentDaySubscription.dispose();
        }

        if (authRepository.isGuestMode()) {
            view.showEmptyState();
            view.showGuestWarning();
            return;
        }

        currentDaySubscription = repository.getPlanByDay(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (meals != null && !meals.isEmpty()) {
                                view.showPlanMeals(meals);
                            } else {
                                view.showEmptyState();
                            }
                        },
                        error -> {
                            view.showError(error.getMessage());
                        }
                );

        disposable.add(currentDaySubscription);
    }

    @Override
    public void removeFromPlanned(Meal meal) {
        disposable.add(
                repository.removeFromPlan(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> view.showRemoveSuccess(meal),
                                error -> view.showError("Failed to remove: " + error.getMessage())
                        )
        );
    }

    @Override
    public Boolean isGuestMode() {
        return authRepository.isGuestMode();
    }

    @Override
    public void onDestroy() {
        disposable.clear();
        view = null;
    }
}