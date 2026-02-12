package com.example.mealwise.presentation.splash.presenter;

import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.presentation.splash.view.SplashView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashPresenter {

    private final SplashView view;
//    private final SharedPrefHelper sharedPrefHelper;
    private final AuthRepository authRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SplashPresenterImpl(SplashView view, AuthRepository authRepository) {
        this.view = view;
//        this.sharedPrefHelper = sharedPrefManager;
        this.authRepository = authRepository;
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
        boolean isFirstTime = authRepository.isFirstTimeLaunch();
        boolean isGuest = authRepository.isGuestMode();
        Object currentUser = authRepository.getCurrentUser();

        android.util.Log.d("SplashDebug", "Is First Time: " + isFirstTime);
        android.util.Log.d("SplashDebug", "Is Guest: " + isGuest);
        android.util.Log.d("SplashDebug", "Current User: " + currentUser);
        if (authRepository.getCurrentUser() != null) {
            view.navigateToHome();
        }
        else if (authRepository.isGuestMode()) {
            view.navigateToHome();
        }
        else if (authRepository.isFirstTimeLaunch()) {
            view.navigateToOnboarding();
        }
        else {
            view.navigateToSignIn();
        }
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}