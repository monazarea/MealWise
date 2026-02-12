package com.example.mealwise.presentation.fav.presneter;

import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.fav.view.FavoritesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FavoritesPresenterImpl implements FavoritesPresenter {

    private FavoritesView view;
    private MealsRepository repository;
    private AuthRepository authRepository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FavoritesPresenterImpl(FavoritesView view, MealsRepository repository,AuthRepository authRepository) {
        this.view = view;
        this.repository = repository;
        this.authRepository = authRepository;
    }

    @Override
    public void getFavorites() {
        if (authRepository.isGuestMode()) {
            view.showEmptyState();
            view.showGuestWarning();
            return;
        }
        disposable.add(
                repository.getFavoriteMeals()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (meals != null && !meals.isEmpty()) {
                                        view.showFavorites(meals);
                                    } else {
                                        view.showEmptyState();
                                    }
                                },
                                error -> view.showError(error.getMessage())
                        )
        );
    }

    @Override
    public void removeFromFavorites(Meal meal) {
        disposable.add(
                repository.removeFromFavorites(meal)
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
    }
}
