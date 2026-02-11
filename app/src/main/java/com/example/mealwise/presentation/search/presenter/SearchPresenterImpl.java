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
                                    throwable -> {
                                        view.hideLoading();
                                        view.showNetworkError();
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
        public void getCategories() {
            view.showLoading();
            disposables.add(
                    repository.getCategories()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    response -> {
                                        view.hideLoading();
                                        view.showCategoriesList(response.getCategories());
                                    },
                                    throwable -> {
                                        view.hideLoading();
                                        view.showNetworkError();
                                    }
                            )
            );
        }

        @Override
        public void getAreas() {
            view.showLoading();
            disposables.add(
                    repository.getAreasList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    response -> {
                                        view.hideLoading();
                                        view.showAreasList(response.getAreas());
                                    },
                                    throwable -> {
                                        view.hideLoading();
                                        view.showNetworkError();
                                    }
                            )
            );
        }
        @Override
        public void getIngredients() {
            view.showLoading();
            disposables.add(
                    repository.getIngredientsList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    response -> {
                                        view.hideLoading();
                                        if (response.getIngredients() != null) {
                                            view.showIngredientsList(response.getIngredients());
                                        }
                                    },
                                    throwable -> {
                                        view.hideLoading();
                                        view.showError(throwable.getMessage());
                                    }
                            )
            );
        }

        @Override
        public void onDestroy() {
            disposables.clear();
        }
    }