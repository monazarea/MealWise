//package com.example.mealwise.data.dp;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.example.designpatternforandroid.data.products.datasource.local.ProductsDao;
//import com.example.designpatternforandroid.data.products.models.ProductsItem;
//
//@Database(entities = {ProductsItem.class},version = 1)
//public abstract class AppDatabase extends RoomDatabase {
//    public abstract ProductsDao productsDao();
//
//    private static AppDatabase INSTANCE;
//
//
//    public static AppDatabase getInstance(Context context){
//        if(INSTANCE == null){
//            INSTANCE = Room.databaseBuilder(context,
//                    AppDatabase.class, "ProductsDB").build();
//        }
//        return INSTANCE;
//    }
//}
