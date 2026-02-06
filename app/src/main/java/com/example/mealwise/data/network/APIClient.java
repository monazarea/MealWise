package com.example.mealwise.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {

    private static Retrofit retrofit;
    private  static  final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    private APIClient(){

    }
    public   static   Retrofit getInstance(){
        if(retrofit == null){
            synchronized (APIClient.class){
                if(retrofit == null){
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return  retrofit;
    }
}

