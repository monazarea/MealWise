package com.example.mealwise.presentation.splash.presenter;

import com.example.mealwise.data.auth.datasource.helpers.SharedPrefHelper;
import com.example.mealwise.presentation.splash.view.SplashView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashPresenter {

    private final SplashView view;
    private final SharedPrefHelper sharedPrefHelper;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SplashPresenterImpl(SplashView view, SharedPrefHelper sharedPrefManager) {
        this.view = view;
        this.sharedPrefHelper = sharedPrefManager;
    }

    @Override
    public void checkNavigationLogic() {
        disposables.add(
                Completable.timer(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::decideDestination)
        );
    }

    private void decideDestination() {
        if (sharedPrefHelper.isFirstTimeLaunch()) {
            view.navigateToOnboarding();
        }
        else if (sharedPrefHelper.isGuestMode() || FirebaseAuth.getInstance().getCurrentUser() != null) {
            view.navigateToHome();
        }
        else {
            view.navigateToAuth();
        }
    }
    @Override
    public void onDestroy() {
        disposables.clear();
    }
}