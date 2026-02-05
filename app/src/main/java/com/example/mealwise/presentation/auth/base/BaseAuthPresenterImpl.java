package com.example.mealwise.presentation.auth.base;




import com.example.mealwise.data.auth.repository.AuthRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseAuthPresenterImpl<V extends BaseAuthView> implements BaseAuthPresenter{

    protected V view;
    protected AuthRepository repository;
    protected CompositeDisposable disposables = new CompositeDisposable();

    public BaseAuthPresenterImpl(V view, AuthRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    @Override
    public void signInWithGoogle(String idToken) {
        // view.showLoading();
        disposables.add(
                repository.signInWithGoogle(idToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.navigateToHome();
                                },
                                error -> {
                                    view.hideLoading();
                                    handleError(error);
                                }
                        )
        );
    }

    protected void handleError(Throwable error) {
        String msg = error.getMessage();
        if (msg != null && msg.contains("User is null")) {
            view.showError("Unknown error occurred");
        } else {
            view.showError(msg);
        }
    }


    @Override
    public void onDestroy() {
        disposables.clear();
    }
}