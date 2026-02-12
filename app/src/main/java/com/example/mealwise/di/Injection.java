package com.example.mealwise.di;

import android.content.Context;

import com.example.mealwise.data.auth.datasource.AuthLocalDataSource;
import com.example.mealwise.data.auth.datasource.AuthLocalDataSourceImpl;
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSource;
import com.example.mealwise.data.auth.datasource.AuthRemoteDataSourceImp;
import com.example.mealwise.data.auth.datasource.helpers.FirestoreHelper;
import com.example.mealwise.data.auth.datasource.helpers.SharedPrefHelper;
import com.example.mealwise.data.auth.repository.AuthRepository;
import com.example.mealwise.data.auth.repository.AuthRepositoryImpl;
import com.example.mealwise.data.meals.datasource.local.MealsLocalDataSource;
import com.example.mealwise.data.meals.datasource.remote.MealsRemoteDataSource;
import com.example.mealwise.data.meals.repository.MealsRepository;
import com.example.mealwise.data.meals.repository.MealsRepositoryImpl;

public class Injection {

    public static AuthRepository provideAuthRepository(Context context) {
        AuthRemoteDataSource remoteDataSource = AuthRemoteDataSourceImp.getInstance();
        AuthLocalDataSource localDataSource = new AuthLocalDataSourceImpl(SharedPrefHelper.getInstance(context));
        return AuthRepositoryImpl.getInstance(remoteDataSource,localDataSource);
    }


    public static SharedPrefHelper provideSharedPrefHelper(Context context) {
        return SharedPrefHelper.getInstance(context);
    }

    public static MealsRepository provideMealsRepository(Context context) {
        return MealsRepositoryImpl.getInstance(MealsRemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(context), FirestoreHelper.getInstance());
    }

}
