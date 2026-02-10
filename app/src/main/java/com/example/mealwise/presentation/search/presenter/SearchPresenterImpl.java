package com.example.mealwise.presentation.search.presenter;

import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.presentation.search.view.SearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchPresenterImpl implements SearchPresenter {

    private SearchView view;
    private MealsRepository repository;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    public SearchPresenterImpl(SearchView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;

        //setupSearchObserver();
    }

    private void setupSearchObserver() {
        disposables.add(
                searchSubject
                        .debounce(600, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .filter(text -> !text.trim().isEmpty())
                        .switchMapSingle(query -> repository.searchMealsByName(query)
                                .subscribeOn(Schedulers.io()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                        view.showMeals(response.getMeals());
                                    } else {
                                        view.showEmptyState();
                                    }
                                },
                                error -> {
                                    view.hideLoading();
                                    view.showError(error.getMessage());
                                }
                        )
        );
    }
    @Override
    public void prepareSearchObserver() {
        disposables.clear();

        setupSearchObserver();
    }

    @Override
    public void onSearchQueryChanged(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.clearResults();
        } else {
            view.showLoading();
            searchSubject.onNext(query.trim());
        }
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}